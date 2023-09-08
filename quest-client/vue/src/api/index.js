import http from "@/utils/request";

//登陆管理
export const loginCheck = (data)=>{
    return http.post('/project/user/Login',data)
}
//重置密码

export const sendemail = (data)=>{
    return http.post('/sendEMail',data)
}
export const reset = (data)=>{
    return http.post('/resetPassword',data)
}
//修改密码
export const change = (data)=>{
    return http.post('/project/user/ChangePassword',data)
}
export const lightExport = ()=>{
    return http.post('/project/award/Excel')
}

//系统管理员的用户管理
export  const adduser = (data) => {
  return http.post('/project/user/AddUser',data)
}
export  const deleteuser = (data) => {
    return http.post('/project/user/DeleteUser',data)
}
export  const searchuser = (data) => {
    return http.post('/project/user/SearchUser',data)
}
export  const updateuser = (data) => {
    return http.post('/project/user/UpdateUser',data)
}
export  const checkuser = (data) => {
    return http.post('/project/user/UserInformation',data)
}


//教师的项目管理
export  const addproject = (data) => {
    return http.post('/project/project/AddProject',data)
}
export  const sumProject = (data) => {
    return http.post('/project/project/SummaryProject',data)
}
export  const searchproject = (data) => {
    return http.post('/project/project/SearchPublishProject',data)
}
export  const search = (data) => {
    return http.post('/project/project/CheckProject',data)
}
export  const update = (data) => {
    return http.post('/project/project/UpdateMyProjectPro',data)
}
export  const deletepro = (data) => {
    return http.post('/project/project/DeleteProject',data)
}
export  const updatepro = (data) => {
    return http.post('/project/project/UpdateProject',data)
}

export  const deleteaward = (data) => {
    return http.post('/project/award/delete',data)
}
export  const deletemo = (data) => {
    return http.post('/project/monograph/delete',data)
}
export  const deletepa = (data) => {
    return http.post('/project/paper/delete',data)
}
export  const deletep = (data) => {
    return http.post('/project/patent/delete',data)
}

export  const updateaward = (data) => {
    return http.post('/project/award/update',data)
}
export  const updatemo = (data) => {
    return http.post('/project/monograph/update',data)
}
export  const updatepa = (data) => {
    return http.post('/project/paper/update',data)
}
export  const updatep = (data) => {
    return http.post('/project/patent/update',data)
}



export  const SearchMyProject = (data) => {
    return http.post('/project/project/SearchMyProject',data)
}
export  const Selectaward = (data) => {
    return http.post('/project/award/selectAll',data)
}
export  const Selectmo = (data) => {
    return http.post('/project/monograph/selectAll',data)
}

export  const Selectpa = (data) => {
    return http.post('/project/paper/selectAll',data)
}
export  const Selectp = (data) => {
    return http.post('/project/patent/selectAll',data)
}
export  const Checkmono = () => {
    return http.post('/project/information/Monograph/checkall')
}
export  const Checkmo = () => {
    return http.post('/project/information/Monograph/check')
}
export  const Checknomo = () => {
    return http.post('/project/information/Monograph/checkno')
}
export  const Checkpaper = () => {
    return http.post('/project/information/Paper/checkall')
}
export  const Checkpap = () => {
    return http.post('/project/information/Paper/check')
}

export  const Checknopap = () => {
    return http.post('/project/information/Paper/checkno')
}
export  const Checkpat = () => {
    return http.post('/project/information/Patent/checkall')
}
export  const Checkpa = () => {
    return http.post('/project/information/Patent/check')
}
export  const Checknopa = () => {
    return http.post('/project/information/Patent/checkno')
}
export  const Checkaward = () => {
    return http.post('/project/information/Award/checkall')
}
export  const Checkaw = () => {
    return http.post('/project/information/Award/check')
}
export  const Checknoaw = () => {
    return http.post('/project/information/Award/checkno')
}


export  const insertaward = (data) => {
    return http.post('/project/award/insert',data)
}
export  const insertmo = (data) => {
    return http.post('/project/monograph/insert',data)
}
export  const insertpa = (data) => {
    return http.post('/project/paper/insert',data)
}
export  const insertp = (data) => {
    return http.post('/project/patent/insert',data)
}




//科研管理员的项目审核及汇总
export  const AuditProject = (data) => {
    return http.post('/project/project/AuditProject',data)
}
export  const CheckProject = (data) => {
    return http.post('/project/project/CheckProject',data)
}
export  const PublishProject = (data) => {
    return http.post('/project/project/PublishProject',data)
}
export  const SearchAuditProject = (data) => {
    return http.post('/project/project/SearchAuditProject',data)
}
export  const SummaryProject = (data) => {
    return http.post('/project/project/SummaryProject',data)
}

export  const checkAward = (data) => {
    return http.post('/project/information/Award/isCheck',data)
}
export  const checkMo = (data) => {
    return http.post('/project/information/Monograph/isCheck',data)
}
export  const checkPaper = (data) => {
    return http.post('/project/information/Paper/isCheck',data)
}
export  const checkPa = (data) => {
    return http.post('/project/information/Patent/isCheck',data)
}


export  const SearchAward = () => {
    return http.post('/project/information/Award/uncheck')}
export  const SearchMo = () => {
    return http.post('/project/information/Monograph/uncheck')}
export  const SearchPaper = () => {
    return http.post('/project/information/Paper/uncheck')}
export  const SearchPa = () => {
    return http.post('/project/information/Patent/uncheck')}
