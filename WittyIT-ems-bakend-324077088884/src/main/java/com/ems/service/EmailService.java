package com.ems.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ems.configuration.PropConfig;
import com.ems.domain.Employee;
import com.ems.domain.LeaveHistory;
import com.ems.domain.LeaveHistory.Status;
import com.ems.domain.Resignation;
import com.ems.domain.ResignationReason;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.servicefinder.utils.UserUtils;
import com.ems.wrappers.ApplyLeaveWrapper;
import com.ems.wrappers.LeaveHistoryWrapper;

/**
 * Service to send emails. A service provides logic to operate on the data sent
 * to and from the Repository layer and the Controller.
 * 
 * @author Avinash Tyagi
 */
@Service
public class EmailService {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PropConfig propConfig;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private JavaMailSender mailSender;
	
	private static final Logger log = Logger.getLogger(EmailService.class.getName());
	
	private static final Locale US_LOCALE=new Locale("US");

	/**
	 * Send password recovery.
	 *
	 * @param Employee         the Employee
	 * @param link             the link
	 * @param recoveryPassword the recovery password
	 * @throws ServiceException the service exception
	 */
	public void sendPasswordRecovery(Employee employee, String link, String recoveryPassword) throws ServiceException {

		employee = employeeService.findByEmail(employee.getOfficialEmail());
		if (employee == null)
			throw new ServiceException("Email not found!");

		employeeService.saveRecoveryPassword(employee, recoveryPassword);

		String loginUrl = propConfig.getApplicationUrl() + "/#/signin";

		Context ctx = new Context(new Locale("US"));
		ctx.setVariable("user", employee);
		ctx.setVariable("link", link);
		ctx.setVariable("loginUrl", loginUrl);
		String htmlContent = this.templateEngine.process("password-recovery-email.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getMailUser());

		sendMail(from, employee, "Password reset", htmlContent, null);
	}

	/**
	 * Send apply leave mail.
	 *
	 * @param user             the user
	 * @param link             the link
	 * @param firmName         the firm name
	 * @param recoveryPassword the recovery password
	 * @throws ServiceException the service exception
	 */
	public void sendLeaveApplyMail(LeaveHistoryWrapper leave) throws ServiceException {
		Employee manager = null;
		try {
			manager = employeeService.findById(leave.getManagerId());
		} catch (Exception e) {
		}
		if (manager == null)
			throw new ServiceException("Manager not found!");

		Context ctx = new Context(new Locale("US"));
		ctx.setVariable("user", manager);
		ctx.setVariable("leave", leave);
		ctx.setVariable("fromDate", DateUtil.removeTime(leave.getFromDate()));
		ctx.setVariable("toDate", DateUtil.removeTime(leave.getToDate()));
		String htmlContent = this.templateEngine.process("leave-apply-info.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getHrMail());

		sendMail(from, manager, "New leave application", htmlContent,
				propConfig.isIncludeCcInLeaveMails() ? propConfig.getLeaveMailCcArrayList() : null);
	}

	public void sendApplyLeaveMail(Employee employee, LeaveHistory leave) throws ServiceException {
		Employee manager = null;
		try {
			manager = employeeService.findById(leave.getManagerId());
		} catch (Exception e) {
		}
		if (manager == null)
			throw new ServiceException("Manager not found!");

		Context ctx = new Context(new Locale("US"));
		ctx.setVariable("user", manager);
		ctx.setVariable("employeeName", UserUtils.toCamelCase(employee.getFullName()));
		ctx.setVariable("leave", leave);
		String value = leave.getDayCount() == .5f ? String.valueOf(leave.getDayCount())
				: String.valueOf(leave.getDayCount().longValue());

		ctx.setVariable("dayCount", value);
		ctx.setVariable("fromDate", DateUtil.removeTime(leave.getFromDate()));
		ctx.setVariable("toDate", DateUtil.removeTime(leave.getToDate()));
		String htmlContent = this.templateEngine.process("leave-apply-info.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getHrMail());

		sendMail(from, manager, "New leave application", htmlContent,
				propConfig.isIncludeCcInLeaveMails() ? propConfig.getLeaveMailCcArrayList() : null);
	}

	/**
	 * Send apply leave mail.
	 *
	 * @param user             the user
	 * @param link             the link
	 * @param firmName         the firm name
	 * @param recoveryPassword the recovery password
	 * @throws ServiceException the service exception
	 */
	public void sendApprovedLeaveMail(LeaveHistoryWrapper leave, boolean isApproved) throws ServiceException {
		log.info(" Sending sendApprovedLeaveMail ");
		Employee employee = null;
		try {
			employee = employeeService.findById(leave.getEmpId());
		} catch (Exception e) {
			log.severe("Error during findById " + e.getMessage());
		}
		if (employee == null)
			throw new ServiceException("Employee not found!");

		String subject = "";
		Context ctx = new Context(US_LOCALE);
		ctx.setVariable("user", employee);
		ctx.setVariable("leave", leave);
		ctx.setVariable("fromDate", DateUtil.removeTime(leave.getFromDate()));
		ctx.setVariable("toDate", DateUtil.removeTime(leave.getToDate()));
		String value = leave.getDayCount() == .5f ? String.valueOf(leave.getDayCount())
				: String.valueOf(leave.getDayCount().longValue());
		ctx.setVariable("dayCount", value);
		// if leave revoked by admin then leave revoked email sent to user
		if (leave.getStatus().equals(Status.REVOKED)) {
			subject = "Leave revoked";
			ctx.setVariable("status", "revoked");
		} else {
			subject = isApproved ? "Leave approved" : "Leave rejected";
			ctx.setVariable("status", isApproved ? "approved" : "rejected");
		}
		String htmlContent = this.templateEngine.process("leave-approve-info.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getHrMail());
		
		sendMail(from, employee, subject, htmlContent,
				propConfig.isIncludeCcInLeaveMails() ? propConfig.getLeaveMailCcArrayList() : null);
	}

	public void sendApplyResignationMail(Employee employee, Resignation resignation, ResignationReason resReason)
			throws ServiceException {
		Employee manager = null;
		try {
			manager = employeeService.findById(employee.getManagerId());
		} catch (Exception e) {
		}
		if (manager == null)
			throw new ServiceException("Manager not found!");

		Context ctx = new Context(new Locale("US"));
		ctx.setVariable("user", manager);
		ctx.setVariable("employeeName", UserUtils.toCamelCase(employee.getFullName()));
		ctx.setVariable("reason", resReason.getReason().toString());
		ctx.setVariable("resignationDate", DateUtil.removeTime(resignation.getCreationDate()));
		String htmlContent = this.templateEngine.process("resignation-apply-info.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getMailUser());

		List<String> ccEmailList = new ArrayList<String>();
//		ccEmailList.add(employee.getOfficialEmail());
//		ccEmailList.add(propConfig.getHrMail());
//		ccEmailList.add(employee.getPersonalEmail());
		sendMail(from, manager, "New resignation letter", htmlContent, ccEmailList);
	}

	/**
	 * Send mail.
	 *
	 * @param from    the from
	 * @param to      the to
	 * @param subject the subject
	 * @param message the message
	 * @param file    the file
	 */
	private void sendMail(final Employee from, final Employee to, final String subject, final String message,
			final List<String> ccList) {
		log.info("inside sendMail");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

				String name = to.getFullName();

				String fromName = "Admin";
				String fromEmail = from.getOfficialEmail();

				String managerMail = to.getOfficialEmail();
				String hrMail = propConfig.getHrMail();

				mimeMessageHelper.setTo(new String[] { managerMail, hrMail });

//				mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO,
//						new InternetAddress(to.getOfficialEmail(), name));

				mimeMessageHelper.setFrom(new InternetAddress(fromEmail, fromName));
				mimeMessageHelper.setSubject(subject);
				InternetHeaders headers = new InternetHeaders();
				headers.addHeader("Content-type", "text/html; charset=UTF-8");

				if (ccList != null) {
					InternetAddress[] recipientAddresses = new InternetAddress[ccList.size()];
					for (int i = 0; i <= ccList.size() - 1; i++)
						recipientAddresses[i] = new InternetAddress(ccList.get(i));

					mimeMessage.setRecipients(javax.mail.Message.RecipientType.CC, recipientAddresses);
				}

				MimeBodyPart body = new MimeBodyPart(headers, message.getBytes("UTF-8"));
				MimeMultipart multipart = new MimeMultipart();
				multipart.addBodyPart(body);
				mimeMessage.setContent(multipart);
			}
		};
		
		this.mailSender.send(preparator);
		log.info("sent mail successfully ");
	}

}
