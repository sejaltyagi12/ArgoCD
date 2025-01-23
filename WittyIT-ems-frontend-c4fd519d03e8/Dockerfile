# Use an official Node.js Alpine image
FROM node:14.21.3

# Set the working directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install Bower globally
RUN npm install -g bower

# Copy other necessary files
COPY . .

# Change to the src directory and run Bower install
WORKDIR /usr/src/app/src
#RUN bower install node --trace-deprecation
RUN bower install --allow-root /bin/bash
 
# Go back to the root directory and run npm install
WORKDIR /usr/src/app
RUN npm install

# Expose the port the app runs on
EXPOSE 9000

# Command to run the application
CMD ["npm", "start"]




# # Use Node.js to build the Angular app
# FROM node:14.21.3 AS build

# # Set the working directory
# WORKDIR /usr/src/app

# # Copy package.json and install dependencies
# COPY package*.json ./
# RUN npm install -g bower
# RUN npm install

# # Copy the rest of the app and build it
# COPY . .
# RUN npm run build --prod

# # Use Nginx to serve the built app
# FROM nginx:alpine

# # Copy the Angular build output to Nginx's public directory
# COPY --from=build /app/dist/your-app-name /usr/share/nginx/html

# # Expose port 80
# EXPOSE 80

# # Run Nginx in the foreground
# CMD ["nginx", "-g", "daemon off;"]

