<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>

<@c.page>

<@p.pager url page/>
<div class="card-columns" id="message-list">
    <#list page.content as user>
        <div class="card m-2" data-id="${user.id}">
            <span>${user.id}</span>
            <i>${user.username}</i>
            <i>${user.age}</i>
            <i>${user.getDateOfBirth()}</i>
            <div class="card-footer text-muted">
                <a href="/user-messages/${user.id}"> ${user.id}</a>
            </div>
        </div>
    <#else>
        No users
    </#list>
</div>
<@p.pager url page/>
</@c.page>

