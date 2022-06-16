<template>
	<view>
		<u--form labelPosition="left" :model="model1" :rules="rules" ref="form1">

			<u-form-item label="账号" prop="userInfo.account" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.account" placeholder="请输入账号" border="none"></u--input>
			</u-form-item>


			<u-form-item label="密码" prop="userInfo.password" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.password" placeholder="请输入密码" type="password" border="none">
				</u--input>
			</u-form-item>


			<u-form-item label="权限" prop="userInfo.authority" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.authority" placeholder="请输入权限,多个逗号隔开" border="none"></u--input>
			</u-form-item>

			<u-form-item label="姓名" prop="userInfo.name" borderBottom ref="item1">
				<u--input v-model="model1.userInfo.name" placeholder="请输入姓名" border="none"></u--input>
			</u-form-item>

		</u--form>
		<u-button class="uButton" @click="uButton()" type="primary" text="注册"></u-button>

	</view>
</template>

<script>

	
	export default {
		data() {
			return {
				model1: {
					userInfo: {
						account: 'txy123456',
						name: '小田技术博客',
						password: 'txy123456',
						authority: 'admin'
					},
				},
				rules: {
					'userInfo.name': {
						type: 'string',
						required: true,
						message: '请填写姓名',
						trigger: ['blur', 'change']
					},
					'userInfo.authority': {
						type: 'string',
						required: true,
						message: '请输入正确的权限',
						trigger: ['blur', 'change']
					},
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
				},
			}
		},
		onShow() {

		},
		methods: {
			uButton() {
				if (this.model1.userInfo.account == "" ||
					this.model1.userInfo.name == '' ||
					this.model1.userInfo.password == '' ||
					this.model1.userInfo.authority == '') {
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
				this.register(pwd)
			},
			
			async register(pwd) {
				const res = await this.$myRequest({
					url: '/user/register',
					method: 'POST',
					data: {
						account: this.model1.userInfo.account,
						password: pwd,
						authority: this.model1.userInfo.authority,
						name: this.model1.userInfo.name
					}
				})
				if(res.data.code == 200){
					uni.showToast({
						title:"注册成功",
						icon:"none"
					})
				}
				if(res.data.code == 400){
					uni.showToast({
						title:res.data.data,
						icon:"none"
					})
				}
			},

		}
	}
</script>

<style lang="scss">
	.uButton {
		margin-top: 50px;
	}
</style>
