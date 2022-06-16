<script>
	export default {
		globalData:{
			skRSAPublicKey: 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJXjjN54zuYgR9Xl/VxQu63X9PgrCENf8C9j7WcyB/+f8cy3zQmIW3h0/auw1oKrcxeNz8rctaFsBiNI7BZlTuMCAwEAAQ=='
		},
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>


<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
</style>