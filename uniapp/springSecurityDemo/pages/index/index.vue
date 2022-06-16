<template>
	<view>
		<view>
			<u-button type="primary" @click="register" text="注册"></u-button>
			<u-divider text=""></u-divider>
			<u-button type="success" @click="login" text="登录"></u-button>
			<u-divider text=""></u-divider>
			<u-button type="success" @click="resource" text="资源"></u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		onLoad() {

		},
		methods: {
			register(){
				uni.navigateTo({
					url:"register/register"
				})
			},
			login(){
				uni.navigateTo({
					url:"login/login"
				})
			},
			resource(){
				uni.navigateTo({
					url:"resource/resource"
				})
			}
		}
	}
</script>

<style lang="scss">
	
</style>
