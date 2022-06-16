<template>
	<view>
		<view>
		<text class="text1">{{token}}</text>
		<u-divider text=""></u-divider>
		<u-button type="error" @click="removeToken()" text="清空Token"></u-button>
		<u-divider text=""></u-divider>
		<u-button type="primary" @click="add()" text="增"></u-button>
		
		<u-divider text=""></u-divider>
		<u-button type="primary" @click="remove()" text="删"></u-button>
		
		<u-divider text=""></u-divider>
		<u-button type="primary" @click="get()" text="查"></u-button>
		
		<u-divider text=""></u-divider>
		<text class="text1">{{data}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:"",
				data:''
			}
		},
		onShow() {
			this.token = uni.getStorageSync("token")
		},
		methods: {
			remove(){
				this.removeres();
			},
			add(){
				this.addres();
			},
			removeToken(){
				uni.removeStorageSync("token")
				this.token = ''
			},
			get(){
				this.getres();
			},
			async getres() {
				const res = await this.$myRequest({
					url: '/resource',
					method: 'GET',
					header:{
						"Authorization":this.token
					}
				})
				this.data = res.data.data
			},
			async addres() {
				const res = await this.$myRequest({
					url: '/resource',
					method: 'POST',
					header:{
						"Authorization":this.token
					}
				})
				this.data = res.data.data
			},
			async removeres() {
				const res = await this.$myRequest({
					url: '/resource/remove',
					method: 'POST',
					header:{
						"Authorization":this.token
					}
				})
				this.data = res.data.data
			},
		}
	}
</script>

<style lang="scss">
	.text1{
		word-break: break-all;
		width: 200px;
		height: 200px;
	}
</style>
