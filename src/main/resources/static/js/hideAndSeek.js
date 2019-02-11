function accToAccTrans() {
    $("#recieverField").hide();
    $("#recieverUserAccountField").show();
    $("input[name=type]").val("SELFTRANS");
    checkState();

}

function accToUserTrans() {
    $("#recieverField").show();
    $("#recieverUserAccountField").hide();
    $("input[name=type]").val("SEND");
    checkState();
}

function checkState() {
    var sender = document.getElementById("TransactSenderAccSelector");
    var reciever = document.getElementById("TransactRecieverAccSelector");
    var submitButton = document.getElementById("TransactSubmitButton");
    var checker = document.getElementById("selfTrans").checked;
    var toUser = document.getElementById("toUserTrans").checked;
    if (checker) {
        if (sender.options[sender.selectedIndex].value === reciever.options[reciever.selectedIndex].value) {
            submitButton.setAttribute("disabled", true);
        } else {
            submitButton.removeAttribute("disabled", true);
        }
    }
    if (toUser) {
        submitButton.removeAttribute("disabled", true);
    }
};



$(function () {
    window.onload = function () {/*
        $('[data-toggle="popover"]').popover({
            container: 'body',
            html: true,
            placement: 'right'
        })*/
        $('input[name="datefilter"]').daterangepicker({
            autoUpdateInput: false,
            locale: {
                cancelLabel: 'Clear'
            }
        });

        $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
            $(this).val(picker.startDate.format('YYYY-MM-DD') + '/' + picker.endDate.format('YYYY-MM-DD'));
        });

        $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
        });

    }
});

(function () {
    var reciever = document.getElementById("TransactRecieverAccSelector");
    var sender = document.getElementById("TransactSenderAccSelector");
    reciever.onchange = checkState;
    sender.onchange = checkState;


    var $table = $('#table');
    $(function () {
        $('#toolbar').find('select').change(function () {
            $table.bootstrapTable('refreshOptions', {
                exportDataType: $(this).val()
            });
        });
    })
})();

$( document ).ready(function() {
    checkState();
});
