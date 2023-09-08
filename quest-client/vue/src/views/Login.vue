<template>
  <div id="login">
    <div id="contain">
      <div id="left_card">
        <h1>Quest 管理系统</h1>
        <span>Quest Management System</span>
      </div>
      <div id="right_card">
        <el-card class="el-card">
          <h2>欢迎登录</h2>
          <form class="login" action="">
            <input v-shake type="text" v-model="form.userId" placeholder="请输入账号">
            <input v-shake type="password" v-model="form.password" placeholder="请输入密码">
          </form>
          <div class="message">
            <span v-html="error"></span>
          </div>
          <el-row>
            <el-button type="primary" @click="doLogin" style="width: 45%;">登陆</el-button>
          </el-row>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import {loginCheck} from "../api/index"

import global from "@/views/global";
export default {
  data: function () {
    return {
      form: {
        userId: '',
        password: '',
      },
      rules: {
        userId: [
          {required: true, message: '请输入账号', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],

      }
    }
  },

  methods: {
    doLogin() {
      console.log(this.form)
      loginCheck(this.form).then((data) => {
        console.log(data)
        if (data.data.success === true) {
          this.$message({
            type: 'success',
            message: '登陆成功'
          });
          global.UserId = this.form.userId;
          global.PassWord = this.form.password
          if (data.data.data.power === "系统管理员") {
            // this.$router.push('/admin/homeAdmin')
            this.$router.push({name: 'homesysAdmin'})
          }
          if (data.data.data.power === "科研管理员") {
            // this.$router.push('/admin/homeAdmin')
            this.$router.push({name: 'homeresAdmin'})
          }
          if (data.data.data.power === "教师") {
            // this.$router.push('/admin/homeAdmin')
            this.$router.push({name: 'homeTeacher'})
          }
        } else {
          this.$message({
            type: 'error',
            message: '账号或密码错误'
          });
        }
      })
    },
  }
}
</script>
<style lang="less" scoped>
@keyframes animate {
  0% {
    filter: hue-rotate(0deg);
  }
  100% {
    filter: hue-rotate(360deg);
  }
}

#login {
  position: relative;
  width: 100vw;
  height: 100vh;
  background-image: url(./../assets/home.jpg);
  background-size: 100% 100%;
  background-color: #a7a8bd;

  #contain {
    height: 400px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 25px;
    border: 1px solid black;
    background-color: rgba(255, 255, 255, 0.1) !important;
    backdrop-filter: blur(5px);
    box-shadow: -5px -5px 10px rgb(39, 65, 65),
    5px 5px 20px aqua;
    /* 5秒 infinite循环播放无限次 linear匀速  */
    animation: animate 5s linear infinite;
  }
}

#contain {
  display: flex;
  flex-direction: row;
  text-align: center;
  align-items: center;

  #left_card {
    width: 500px;

    h1 {
      color: white;
      white-space: nowrap;
    }

    span {
      font-size: 1.2rem;
      text-align: center;
      color: white;
      white-space: nowrap;
    }
  }

  #right_card {
    width: 400px;

    .el-card {
      margin: 0 45px;
      border-radius: 25px;
      background-color: rgba(255, 255, 255, 0.1);
    }
  }
}

#right_card {
  display: flex;
  justify-content: center;
  align-items: center;

  h2 {
    margin-bottom: 5px;
  }

  .login {
    input {
      width: 80%;
      height: 45px;
      margin-top: 10px;
      border: 1px solid white;
      background-color: rgba(255, 255, 255, 0.5);
      border-radius: 10px;
      font-size: inherit;
      padding-left: 20px;
      outline: none;
    }
  }

  .remember {
    float: right;
    height: 26px;
    text-align: center;
    font-size: 1rem;
    position: relative;

    .radio {
      height: 1rem;
      width: 1rem;
      vertical-align: middle;
      margin-top: -2px;
      opacity: 0;
    }

    label {
      position: absolute;
      left: -2px;
      top: 5px;
      height: 1rem;
      width: 1rem;
      vertical-align: middle;
      margin-top: -2px;
      border-radius: 50%;
      border: 1px solid black;
    }

    //radio选中后修改labe内的内容 :after 选择器在被选元素的内容后面插入内容。
    input:checked + label::after {
      content: "";
      width: 0.6rem;
      height: 0.6rem;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      border-radius: 50%;
      background-color: rgba(207, 38, 38, 0.8);
      border: 1px solid rgba(207, 38, 38, 0.8);
    }
  }

  .message {
    margin-top: 26px;
    font-size: 0.9rem;
    color: red;
  }

  .loginbtn {
    width: 100%;
    height: 35px;
    margin-top: 10px;
    border-radius: 10px;
    background-color: rgba(207, 38, 38, 0.8);
  }

}
</style>
