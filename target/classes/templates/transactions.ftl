<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>
<#import "parts/actions.ftl" as act>

<#macro TransTable>

</#macro>
<@c.page>

    <@p.pager url page/>
    <table class="table" id="transaction-list">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">DATE</th>
            <th scope="col">AMMOUNT</th>
            <th scope="col">SENDER</th>
            <th scope="col">RECIEVER</th>
        </tr>
        </thead>
        <tbody>

        <#list page.content as trans>
            <#if (user.id=trans.sender.id || user.id=trans.reciever.id)>
                <tr>
                    <th scope="row">${trans.id}</th>
                    <td>${trans.date}</td>
                    <td>${trans.ammount}</td>
                    <td>
                        <#if trans.getSenderAccount()??> ${trans.sender.getUsername()}
                        <#else></#if>
                        <#if trans.getSenderAccount()??>(ACCOUNT# ${trans.getSenderAccount().getId()})
                        <#else>АВТОПОПОЛНЕНИЕ</#if></td>
                    <td>${trans.reciever.getUsername()}</td>
                </tr>
            </#if>
            <#if isAdmin &&id??>
                <tr>
                    <th scope="row">${trans.id}</th>
                    <td>${trans.date}</td>
                    <td>${trans.ammount}</td>
                    <td>
                        <#if trans.getSenderAccount()??> ${trans.sender.getUsername()}
                        <#else></#if>
                        <#if trans.getSenderAccount()??>(ACCOUNT# ${trans.getSenderAccount().getId()})
                        <#else>АВТОПОПОЛНЕНИЕ</#if></td>
                    <td>${trans.reciever.getUsername()}</td>
                </tr>
            </#if>
        <#else>
            Транзакций нет
        </#list>
        </tbody>
    </table>
    <@p.pager url page/>

<div class="container">
    <div class="row">
    <@act.addMoney/>
    <@act.newAcc/>
    <@act.transactTo/>
    </div>
</div>

</@c.page>
