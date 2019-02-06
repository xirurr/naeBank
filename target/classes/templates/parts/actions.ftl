<#include "security.ftl">


<#macro newAcc>
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
</#macro>


<#macro transactTo>
    <a class="btn btn-primary" data-toggle="collapse" href="#transactForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        new trans
    </a>
    <div class="collapse <#--<#if message??>show</#if>-->" id="transactForm">
        <form class="form" method="post" action="/transactions/new">
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">сумма перевода</label>
                <input class="form-control" type="number" min="0" step="0.01" name="ammount"
                       placeholder="Input ammount"/>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">рассчетный счет отправителья</label>
                <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                        name="senderAccount">
                    <#list accounts as account>
                        <option data-subtext="ACC:${account.tag!"  "} Currency:${account.ammount}">${account.id}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">пользователь получатель</label>
                <select class="selectpicker form-control" data-show-subtext="true" data-live-search="true"
                        name="reciever">
                    <#list users as userA>
                        <#if name != userA.username>
                            <option data-subtext="${userA.username}">${userA.id}</option>
                        </#if>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary" type="submit">Transcat</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </form>
    </div>
</#macro>





