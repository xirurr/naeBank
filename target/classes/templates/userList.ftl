<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div>
        <a class="btn btn-primary" data-toggle="collapse" href="#addUserFomr" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            create new user
        </a>
        <div class="collapse <#--<#if message??>show</#if>-->" id="addUserFomr">
            <@l.login "/registration" true "/userList"/>
        </div>
    </div>
    <@p.pager url page/>
    <table class="table" id="transaction-list">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">username</th>
            <th scope="col">AGE</th>
            <th scope="col">Date of birth</th>
            <th scope="col">Adress</th>
            <th scope="col">Transactions</th>
            <th scope="col">Accounts</th>
            <th scope="col">Summary</th>

        </tr>
        </thead>
        <tbody>
        <#list page.content as user>
            <tr>
                <th scope="row">${user.id}</th>
                <td><a href="/accounts/${user.id}">${user.username}</td>
                <td>${user.age}</td>
                <td>${user.getDateOfBirth()}</td>
                <td>${user.adress!""}</td>
                <td><a href="/transactions/${user.id}"> transactions list</a></td>
                <td><a href="/accounts/${user.id}"> accounts list</a></td>
                <td>${user.summ}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <@p.pager url page/>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

</@c.page>

