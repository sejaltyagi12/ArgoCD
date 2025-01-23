/* =========================================================
 * Module: pieChartsController.js
 * Makes pie charts
 * =========================================================
 */

 (function () {
    'use strict';

    angular.module('app')
    .controller('pieChartsCtrl', ['$state',  'EncApiService', '$http', 'apiEndpoints',
        function ($state, EncApiService, $http, apiEndpoints) {

            var vm = this;
            vm.empCount = 0;

         /*
        * common options for pie charts
        */
        var charts = ['company', 'department', 'designation'];

        var commonOptions = {
            chart: {
                backgroundColor: 'transparent',
                style: {
                    fontFamily: 'Lato Regular',
                    
                }
            },
            title: {
                text: 'Company',
                align: 'center',
                
                useHTML: true,
                style: {"fontWeight": "bold", "fontSize": "20px"}
                
            },
            legend: {

                enabled : false
                
                
            },
            tooltip: {
             
             useHTML: true,
             formatter: function() {
                
                return '<div style="text-align:center;"><b>'+this.key +'</b><br>\
                '+this.y +' ('+this.percentage.toFixed(2) +'%)</div>';
                
                
            }
        },
        
        exporting: false,
        credits: false,
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled : false,
                    
                },
                point: {
                    events: {
                        legendItemClick: function () {
                            return false;
                        }
                    }
                },
                states: {
                    hover: {
                        enabled: false,
                        halo: {
                            size: 0
                        }
                    }
                },
                series: {
                    marker: {
                        enabled: true
                    }
                },
                
            }
        },

        series: [{
            type: 'pie',
            innerSize: '50%',
            
            data: [
            
            ],
            showInLegend:true
        }]
    };


    EncApiService('chartData').then(function(response) {
        var companyData = response.data.company;
        for(var i=0 ; i<= response.data.company.length-1 ; i++ ){
           vm.empCount += parseInt(response.data.company[i].count);
       }
       var departmentData = response.data.department;
       var designationData = response.data.designation;

       charts.forEach(function (value) {
        switch(value){

            case 'company':
            var commonOptionsCopy = angular.copy(commonOptions);
            commonOptionsCopy.chart.renderTo = value;
            commonOptionsCopy.title.text = "Company";
            commonOptionsCopy.legend.labelFormatter = function(){
                for(var i=0; i<= companyData.length-1 ; i++){
                   if(this.x == i)
                       return  companyData[i].name;  
               }
           };
           for(var i=0; i<= companyData.length-1 ; i++ ){
               commonOptionsCopy.series[0].data.push({name: companyData[i].name ,y: companyData[i].count});
           }
           Highcharts.chart(commonOptionsCopy);
           break;

           case 'designation':
           var commonOptionsCopy2 = angular.copy(commonOptions);
           commonOptionsCopy2.chart.renderTo = value;
           commonOptionsCopy2.title.text = "Designation";
           commonOptionsCopy2.legend.labelFormatter = function(){
               for(var i=0; i<= designationData.length-1 ; i++){
                   if(this.x == i)
                       return  designationData[i].name;  
               }
           };
           for(var i=0; i<= designationData.length-1 ; i++ ){
            commonOptionsCopy2.series[0].data.push({name: designationData[i].name ,y: designationData[i].count});
        }
        Highcharts.chart(commonOptionsCopy2);
        break;

        case 'department':
        var commonOptionsCopy3 = angular.copy(commonOptions);
        commonOptionsCopy3.chart.renderTo = value;
        commonOptionsCopy3.title.text = "Department";
        commonOptionsCopy3.legend.labelFormatter = function(){
           for(var i=0; i<= departmentData.length-1 ; i++){
               if(this.x == i)
                   return departmentData[i].name;  
           }
           
       };
       for(var i=0; i<= departmentData.length-1 ; i++ ){
        
          commonOptionsCopy3.series[0].data.push({name: departmentData[i].name ,y: departmentData[i].count});
          
      }
      Highcharts.chart(commonOptionsCopy3);
      break;
  }
});

   },
   function (err) {

   });

    

    
}]);
})();