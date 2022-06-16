<template>
	<view class="top">
		<u--form labelPosition="left" :model="model1" :rules="rules" ref="form1">

			<u-form-item label="账号" prop="userInfo.account" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.account" placeholder="请输入账号" border="none"></u--input>
			</u-form-item>


			<u-form-item label="密码" prop="userInfo.password" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.password" placeholder="请输入密码" type="password" border="none">
				</u--input>
			</u-form-item>
			
			<u-form-item label="验证码" prop="userInfo.code" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.code" placeholder="请输入密码" type="none" border="none">
				</u--input>
			</u-form-item>
		</u--form>
		<u--image :lazy-load="true" :showLoading="true" :src="captcherImg" @click="uimageclick()" width="220px" height="80px"></u--image>
		<u-button class="uButton" @click="uButton()" type="primary" text="登录"></u-button>
		<text class="text1" >{{loginData}}</text>
		<u-divider text=""></u-divider>
		<u-button  @click="gores()" type="primary" text="资源管理"></u-button>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				captcherImg:'',
				userKey:'',
				loginData: '',
				model1: {
					userInfo: {
						account: '',
						password: '',
						code:''
					},
				},
				rules: {
					'userInfo.password': {
						type: 'string',
						required: true,
						message: '请输入正确的密码',
						trigger: ['blur', 'change']
					},
					'userInfo.account': {
						type: 'string',
						required: true,
						message: '请填写账号数字组成',
						trigger: ['blur', 'change']
					},
					'userInfo.code': {
						type: 'string',
						required: true,
						message: '请填写账号数字组成',
						trigger: ['blur', 'change']
					}
				},
			}
		},
		onShow() {
			//验证码获取
			this.getyanzhengma();
		},
		methods: {
			gores(){
				uni.navigateTo({
					url:"../resource/resource"
				})
			},
			uimageclick(){
				this.getyanzhengma();
			},
			async getyanzhengma() {
				const res = await this.$myRequest({
					url: '/captcha',
					method: 'GET'
				})
				this.captcherImg = res.data.data.captcherImg
				this.userKey =  res.data.data.userKey
				this.captcherImg = this.captcherImg.replace(/[\r\n]/g, "");
			},
			uButton() {
				if (this.model1.userInfo.account == "" ||
					this.model1.userInfo.password == '') {
					uni.showToast({
						title: "请提交正常表单",
						icon: "none"
					})
					return;
				}
				//加密
				const encryptor = new this.$jsencrypt();
				const publicKey = getApp().globalData.skRSAPublicKey
				encryptor.setPublicKey(publicKey)
				// 这个就是rsa加密后的值
				const pwd = encryptor.encrypt(this.model1.userInfo.password)
				this.login(pwd)
			},

			async login(pwd) {
				const res = await this.$myRequest({
					url: '/login',
					method: 'POST',
					data: {
						username: this.model1.userInfo.account,
						password: pwd,
						userKey:this.userKey,
						code:this.model1.userInfo.code
					}
				})
				this.loginData = res.data.data
				uni.setStorageSync("token",this.loginData)
				//保存起来
				if (res.data.code == 200) {
					uni.showToast({
						title: "登录成功",
						icon: "none"
					})
				}
				if (res.data.code == 400) {
					uni.showToast({
						title: res.data.data,
						icon: "none"
					})
				}
			},

		}
	}
</script>

<style lang="scss">
	.top{
		width: 750rpx;
	}
	.uButton {
		margin-top: 50px;
	}
	.text1{
		word-break: break-all;
		width: 200px;
		height: 200px;
	}
</style>
