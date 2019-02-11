<#include "security.ftl">
<#macro newAcc userD="!$nouser">
    <form action=<#if userD?is_string>"/accounts/new"<#else>"/accounts/${userD}/new/"</#if>method="post">
    <div class="form-group row">
        <label class="col col-form-label">Account</label>
            <input class="form-control"
                   type="text" name="tag" placeholder="Account name"/>
    </div>
    <button class="btn btn-primary" type="submit">CREATE</button>
<#--
    <input type="hidden" name="IDuser" value="<#if userD?is_string>${user}<#else>${userD}</#if>"/>
-->
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro transactTo>
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
    <form class="form needs-validation" novalidate method="post" name="test" id="transactForm"
          action="/transactions/new">
        <div class="form-group row">
            <label class="col col-form-label">сумма перевода</label>
                <input class="form-control ${(ammountError??&&(SELFTRANSError??||SENDError??))?string('is-invalid','')}"
                       id="TransactAmmountform" type="number" min="1" step="0.01" name="ammount"
                       placeholder="Input ammount" required/>
                <#if ammountError??>
                    <div class="invalid-tooltip">
                        ${ammountError}
                    </div>
                </#if>
        </div>
        <div class="form-group row">
            <label class="col col-form-label">С рассчетного счета:</label>

                <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                        id="TransactSenderAccSelector" name="senderAccount">
                    <#list accounts as account>
                        <option data-subtext="${account.tag!"  "} Currency:<b>${account.ammount}</b>">${account.id}</option>
                    </#list>
                </select>
        </div>
        <div class="form-group row" id="recieverUserAccountField">
            <label class="col col-form-label">на рассчетный счет:</label>
                <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                        id="TransactRecieverAccSelector" name="recieverAccount">
                    <#list accounts as account>
                        <option data-subtext="${account.tag!"  "} Currency:<b>${account.ammount}</b>">${account.id}</option>
                    </#list>
                </select>
        </div>
        <div class="form-group row" id="recieverField" style="display: none;">
            <label class="col col-form-label">пользователю:</label>
                <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                        id="TransactUserNameSelector" name="reciever">
                    <#list users as userA>
                        <#if name != userA.username>
                            <option data-subtext="<b>${userA.username}</b>">${userA.id}</option>
                        </#if>
                    </#list>
                </select>
        </div>

        <button class="btn btn-primary" id="TransactSubmitButton" type="submit">Transcat</button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="type" value="SELFTRANS"/>
    </form>
</#macro>

<#macro addMoney>
    <form class="form" method="post" action="/transactions/new">
        <div class="form-group row">
            <label class="col col-form-label">сумма пополения</label>
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
            <label class="col col-form-label">рассчетный счет отправителья</label>
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

</#macro>






