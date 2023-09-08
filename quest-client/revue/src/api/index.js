import http from "@/utils/request";

// 管理员登录
export const login = (data) =>{
    return http.post('/project/admin-table/adminLogin',data)
}
// 查看所有用户
export const getUserInfo = (data) =>{
    return http.post('/project/user-table/checkUserList',data)
}
// 删除用户
export const deleteUserInfo = (data) =>{
    return http.post('',data)
}
// 查看用户任务
export const getUserTask = (data) =>{
    return http.post('/project/r-user-quest-table/checkUserQuest',data)
}
// 查看副本
export const getUserDung = (data) =>{
    return http.post('/project/dungeon-table/checkDungeonList',data)
}
// 删除副本
export const deleteUserDung = (data) =>{
    return http.post('/project/dungeon-table/deleteDungeon',data)
}
// 查看副本可选任务
export const getUserDungTask = (data) =>{
    return http.post('/project/r-dungeon-quest-table/checkDungeonQuest',data)
}
// 增加副本可选任务
export const addUserDungTask = (data) =>{
    return http.post('/project/r-dungeon-quest-table/addDungeonQuest',data)
}
// 集成副本
export const intUserDungTask = (data) =>{
    return http.post('/project/r-dungeon-quest-table/dungeonIntegration',data)
}
// 查看通知
export const getNote = (data) =>{
    return http.post('/project/note-table/checkNoteList',data)
}
// 增加通知
export const addNote = (data) =>{
    return http.post('/project/note-table/addNote',data)
}
