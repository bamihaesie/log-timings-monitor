
$(function() {

    var options = {
        lines: {
            show: true
        },
        points: {
            show: false
        },
        xaxis: {
            mode: "time"
        }
    };

    $("button.dataUpdate").click(function () {

        range = $("#range-selector").find(":selected").val();

        var selectedServices = '';
        $.each($("#serviceNameSelector input"), function() {
            if (this.checked) {
                selectedServices += "'" + this.getAttribute('value') + "',";
            }
        });
        var len = selectedServices.length;
        if (selectedServices.substr(len - 1, 1) == ",") {
            selectedServices = selectedServices.substring(0, len - 1);
        }

        function onDataReceived(series) {

            var choiceContainer = $("#choices");

            choiceContainer.empty();

            $.each(series, function(key, val) {
                choiceContainer.append("<p class='service-label' name='" + val.serviceName + "'>" + val.serviceName + "</p>");
                choiceContainer.append("<div style='width:900px;height:400px'></div>");
            });

            var serviceSelector = $("#service-selector");
            serviceSelector.find("input").click(plotAccordingToChoice);

            function arrayContainsValue(arr, val) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].serviceName == val) {
                        return arr[i].data;
                    }
                }
                return null;
            }

            function plotAccordingToChoice() {
                var serviceName = $(this).attr("name");
                var div = $("p.service-label[name=" + serviceName + "]").next();
                if ($(this).is(':checked')) {
                    div.show();
                } else {
                    div.hide();
                }
            }

            function plotAccordingToChoices() {

                var data = [];

                choiceContainer.find("p.service-label").each(function () {
                    var key = $(this).text();
                    if (key) {
                        var value = arrayContainsValue(series, key);
                        if (value) {
                            var data = [];

                            for (var i = 0; i < value.length; i++) {
                                data.push(value[i]);
                            }

                            var dateTokens = range.split(new RegExp(/-/));
                            var year = dateTokens[0];
                            var month = dateTokens[1];
                            var day = dateTokens[2];

                            var today = new Date();
                            today.setFullYear(year, month - 1, day);
                            today.setUTCHours(0);
                            today.setMinutes(0);
                            today.setSeconds(0);

                            var tomorrow = new Date();
                            tomorrow.setDate(today.getDate() + 1);
                            tomorrow.setUTCHours(0);
                            tomorrow.setMinutes(0);
                            tomorrow.setSeconds(0);

                            if (data.length > 0) {
                                $.plot($(this).next(), data, {
                                    yaxis: {
                                        min: 0.1
                                    },
                                    xaxis: {
                                        mode: "time",
                                        timezone: "local",
                                        minTickSize: [1, "hour"],
                                        min: today.getTime(),
                                        max: tomorrow.getTime()
                                    },
                                    points: {
                                        show: true,
                                        symbol: "cross",
                                        radius: 2
                                    }
                                });
                            }
                        }
                    }
                });

            }

            plotAccordingToChoices();
        }

        $.ajax({
            url: "http://localhost:9000/timings?range=" + range + "&services=" + selectedServices,
            type: "GET",
            dataType: "json",
            success: onDataReceived
        });

    });
});


$(document).ready(function () {

    $('a#inline').bind("ajaxSend", function() {
        $(this).click();
    }).bind("ajaxComplete", function() {
        $.fancybox.close();
    });

    $("a#inline").fancybox({
        closeClick  : false,
        closeBtn : false,
        helpers   : {
            overlay : {
                closeClick: false,
                opacity: 0.3
            }
        }
    });

    $('a#services').fancybox({
         closeClick  : false,
         closeBtn : true,
         helpers   : {
             overlay : {
                 closeClick: false,
                 opacity: 0.3
             }
         }
    });

    $('a#selectAllServices').click( function () {
        $('#serviceNameSelector input').attr('checked', 'checked');
    });

    $('a#deselectAllServices').click( function () {
        $('#serviceNameSelector input').removeAttr('checked');
    });

});
