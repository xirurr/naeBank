<#include "security.ftl">


<#macro newAcc>
    <div class="col-sm">
        <a class="btn btn-primary" data-toggle="collapse" href="#addAccForm" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            create new account
        </a>
        <div class="collapse <#--<#if message??>show</#if>-->" id="addAccForm">
            <form action="/account/new" method="post">
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">Account</label>
                    <div class="col-sm-5">
                        <input class="form-control"
                               type="text" name="tag" placeholder="Account name"
                        />
                    </div>
                </div>
                <button class="btn btn-primary" type="submit">CREATE</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
</#macro>

<#macro transactTo>
    <div class="col-sm">
        <a class="btn btn-primary" data-toggle="collapse" href="#transactForm" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            transact money
        </a>

        <div class="collapse <#if SELFTRANSError??>show</#if><#if SENDError??>show</#if>" id="transactForm">

            <div class="form-check">
                <input class="form-check-input" onclick="accToAccTrans()" type="radio" name="transType" id="selfTrans"
                       value="option1" checked>
                <label class="form-check-label" for="selfTrans">
                    Transaction over my accounts
                </label>
            </div>
            <div class="form-check">
                <input class="form-check-input" onclick="accToUserTrans()" type="radio" name="transType"
                       id="toUserTrans" value="option2">
                <label class="form-check-label" for="toUserTrans">
                    Transaction to other clients
                </label>
            </div>

            <form class="form needs-validation" novalidate method="post" name="test" id="transactForm" action="/transactions/new">
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">сумма перевода</label>
                    <div class="col-sm-5">
                        <input class="form-control ${(ammountError??&&(SELFTRANSError??||SENDError??))?string('is-invalid','')}"
                               id="TransactAmmountform" type="number" min="1" step="0.01" name="ammount"
                               placeholder="Input ammount" required/>
                        <#if ammountError??>
                            <div class="invalid-tooltip">
                                ${ammountError}
                            </div>
                        </#if>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">С рассчетного счета:</label>
                    <div class="col-sm-5">
                        <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                                id="TransactSenderAccSelector" name="senderAccount">
                            <#list accounts as account>
                                <option data-subtext="${account.tag!"  "} Currency:<b>${account.ammount}</b>">${account.id}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group row" id="recieverUserAccountField">
                    <label class="col-sm-2 col-form-label">на рассчетный счет:</label>
                    <div class="col-sm-5">
                        <select class="selectpicker form-control"  data-show-subtext="true" data-live-search="true"
                                id="TransactRecieverAccSelector" name="recieverAccount"
                                data-toggle="popover" data-trigger="hover" data-content="Првоерьте аккаунты"
                        >
                            <#list accounts as account>
                                <option data-subtext="${account.tag!"  "} Currency:<b>${account.ammount}</b>">${account.id}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row" id="recieverField" style="display: none;">
                    <label class="col-sm-2 col-form-label">пользователю:</label>
                    <div class="col-sm-5">
                        <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                              id="TransactUserNameSelector"  name="reciever">
                            <#list users as userA>
                                <#if name != userA.username>
                                    <option data-subtext="<b>${userA.username}</b>">${userA.id}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                </div>


                <button class="btn btn-primary" id="TransactSubmitButton" type="submit">Transcat</button>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="hidden" name="type" value="SELFTRANS"/>
            </form>
        </div>


    </div>
    <script>
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
        function checkState () {
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
            if (toUser){
                submitButton.removeAttribute("disabled", true);
            }
        };
        (function () {
            var reciever = document.getElementById("TransactRecieverAccSelector");
            var sender = document.getElementById("TransactSenderAccSelector");
            window.onload=checkState;
            reciever.onchange=checkState;
            sender.onchange=checkState;
        })();
    </script>
</#macro>

<#macro addMoney>
    <div class="col-sm">
        <a class="btn btn-primary" data-toggle="collapse" href="#addMoneyForm" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            add money
        </a>
        <div class="collapse ${(ADDFOUNDSError??)?string('show','')}" id="addMoneyForm">
            <form class="form" method="post" action="/transactions/new">
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">сумма пополения</label>
                    <input class="form-control  ${(ammountError??&&ADDFOUNDSError??)?string('is-invalid','')}"
                           type="number" min="0" step="0.01" name="ammount" id="addMoneyAmmountForm"
                           placeholder="Input ammount" required/>
                    <#if ammountError??>
                        <div class="invalid-tooltip">
                            ${ammountError}
                        </div>
                    </#if>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">рассчетный счет отправителья</label>
                    <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                            name="recieverAccount">
                        <#list accounts as account>
                            <option data-subtext="${account.tag!"  "} Currency:<b>${account.ammount}</b>">${account.id}</option>
                        </#list>
                    </select>
                </div>
                <button class="btn btn-primary" type="submit">Transcat</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="hidden" name="type" value="ADDFOUNDS"/>
            </form>
        </div>
    </div>
</#macro>





