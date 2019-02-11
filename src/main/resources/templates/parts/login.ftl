<#include "security.ftl">
<#macro login path isRegisterForm outerUrl="nourl">
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-5">
                <input class="form-control ${(usernameError??)?string('is-invalid','')}"
                       type="text" name="username" placeholder="Username"
                       value="<#if user??>${user.username}</#if>"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-5">
                <input class="form-control ${(passwordError??)?string('is-invalid','')}" type="password" name="password"
                       placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Password:</label>
                <div class="col-sm-5">
                    <input class="form-control ${(password2Error??)?string('is-invalid','')}" type="password"
                           name="password2" placeholder="retype Password"/>
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Дата рождения:</label>
                <div class="col-sm-5">
                    <input class="form-control ${(dateError??)?string('is-invalid','')}" type="date"
                           name="inputDate"
                           value="<#if user??><#if dateError??>${inputDate}</#if></#if>"/>
                    <#if dateError??>
                        <div class="invalid-feedback">
                            ${dateError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Адресс:</label>
                <div class="col-sm-5">
                    <ymaps><input id="suggest1" class="form-control ${(adressError??)?string('is-invalid','')}"
                                  name="adress" data-suggest="true"/>
                        <#if adressError??>
                            <div class="invalid-feedback">
                                ${adressError}
                            </div>
                        </#if>
                    </ymaps>
                </div>
            </div>
        </#if>
        <#if !isRegisterForm>
            <a href="/registration">Registration</a>
        </#if>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>SIGN UP<#else>SIGN IN</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="outerUrl" value=${outerUrl}/>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <button class="btn btn-primary" type="submit"><#if user??>Log out<#else>Log in</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>