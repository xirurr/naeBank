<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>
<#import "parts/actions.ftl" as act>


<@c.page>
    <#if message??>
        <div class="alert alert-success" role="alert">
            ${message}
        </div>
    </#if>

    <div class="container-fluid">
    <div class="row">
    <div class="col">
        <@p.pager url page/>
    </div>
    <div class="col">
        <#if !XMD??>
            <div class="container-fluid">
                <div class="btn-group" role="group">
                    <div class="col">
                        <a class="btn btn-primary" data-toggle="collapse" href="#addMoneyForm" role="button"
                           aria-expanded="false"
                           aria-controls="collapseExample">
                            add more money
                        </a>
                        <div class="collapse ${(ADDFOUNDSError??)?string('show','')}" id="addMoneyForm">
                            <@act.addMoney/>
                        </div>
                    </div>
                    <div class="col">
                        <a class="btn btn-primary" data-toggle="collapse" href="#addAccForm" role="button"
                           aria-expanded="false"
                           aria-controls="collapseExample">
                            create new account
                        </a>
                        <div class="collapse &lt;#&ndash;<#if message??>show</#if>&ndash;&gt;" id="addAccForm">
                            <@act.newAcc/>
                        </div>
                    </div>
                    <div class="col">
                        <a class="btn btn-primary" data-toggle="collapse" href="#transactForm" role="button"
                           aria-expanded="false"
                           aria-controls="collapseExample">
                            transact money
                        </a>
                        <div class="collapse <#if SELFTRANSError??||ammountError??||SENDError??>show</#if>"
                             id="transactForm">
                            <@act.transactTo/>
                        </div>
                    </div>
                </div>
            </div>
        </#if>
    </div>
    <div class="col">
    <a class="btn btn-secondary" data-toggle="collapse" href="#filterForm" role="button"
       aria-expanded="false"
       aria-controls="collapseExample">
        Filter
    </a>
<a class="btn btn-danger" href="
<#if !isAdmin>
    ${url}
<#else >
<#if url??>
        <#if id??>
            <#if url?index_of(id?string.number) gt 0>
                ${url}
            <#else>
                ${url}/${id}
                </#if>
            <#else >
               ${url}
                </#if>
            <#else >
                /${id!""}
            </#if></#if>" role="button">
            ClearFilter
            </a>
            <form class="form collapse" novalidate method="post" name="test" id="filterForm"
                  action="/transactions/<#if id??>${id}/filter<#else>filter/all</#if>">
                <div class="form-group row">
                    <label class="col col-form-label">ID</label>
                    <input class="form-control" type="text" name="idFilter" value=""/>
                    <label class="col col-form-label">даты</label>

                    <input class="form-control" type="text" name="datefilter" value=""/>

                    <label class="col col-form-label">ammount</label>

                    <input class="form-control" type="number" min="1" step="0.01" name="ammount"
                           placeholder="Input ammount" required/>

                    <label class="col col-form-label">sender</label>
                    <div class="input-group">
                        <input class="form-control" type="text" name="senderFilter" id="senderFilter" value=""/>
                        <div class="input-group-prepend">
                            <div class="input-group-text btn btn-primary" id="autoCheckButton">АВТОПОПОЛНЕНИЕ</div>
                        </div>

                    </div>

                    <label class="col col-form-label">reciver</label>

                    <input class="form-control" type="text" name="recieverFilter" value=""/>

                </div>
                <button class="btn btn-primary" type="submit">filter now</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            </form>
            </div>
            </div>


            </div>

            <div class="col">
                <table id="table" data-toggle="table"
                       data-search="false"
                       data-filter-control="true"
                       data-click-to-select="true"
                       data-toolbar="#toolbar">
                    <thead>
                    <tr>
                        <th data-field="id" data-sortable="true">ID</th>
                        <th data-field="date" data-sortable="true">DATE</th>
                        <th data-field="ammount" data-sortable="true">Ammount</th>
                        <th data-field="sender" data-sortable="true">Sender</th>
                        <th data-field="reciver" data-sortable="true">Reciever</th>
                    </tr>

                    </thead>
                    <tbody>
                    <#list page.content as trans>
                        <#if (user.id=trans.sender.id || user.id=trans.reciever.id) && !XMD??>
                            <tr>
                                <td>${trans.id}</td>
                                <td>${trans.date}</td>
                                <td>${trans.ammount?string[".00"]}</td>
                                <td>
                                    <#if trans.getSenderAccount()??>user: ${trans.sender.username}  acc# (${trans.getSenderAccount().getId()})
                                    <#else>АВТОПОПОЛНЕНИЕ</#if></td>

                                <td><#if trans.getSenderAccount()??>
                                        <#if trans.getSender().getId()==trans.getReciever().getId()>
                                            (acc#  ${trans.getRecieverAccount().getId()})
                                        <#else >${trans.reciever.getUsername()}
                                        </#if>
                                    <#else >
                                        (acc#  ${trans.getRecieverAccount().getId()})
                                    </#if>
                                </td>
                            </tr>
                        </#if>
                        <#if isAdmin &&XMD??>
                            <tr>
                                <td>${trans.id}</td>
                                <td>${trans.date}</td>
                                <td>${trans.ammount?string[".00"]}</td>
                                <td>
                                    <#if trans.getSenderAccount()??> ${trans.sender.getUsername()}
                                    <#else></#if>
                                    <#if trans.getSenderAccount()??>(acc# ${trans.getSenderAccount().getId()})
                                    <#else>АВТОПОПОЛНЕНИЕ</#if></td>
                                <td>${trans.reciever.getUsername()} (acc# ${trans.getRecieverAccount().getId()})</td>
                            </tr>
                        </#if>
                    <#else>
                        Транзакций нет
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="col">
                <@p.pager url page/>
            </div>

</@c.page>
