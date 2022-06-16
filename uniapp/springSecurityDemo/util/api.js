const BESE_URL = 'http://127.0.0.1:1009'
//const BESE_URL = 'https://wx2.lishisxk.com/api/'
export const myRequest = (options) => {
	return new Promise((resolve, reject) => {
		uni.request({
			url: BESE_URL + options.url,
			method: options.method || 'GET',
			data: options.data || {},
			header: options.header || {},
			success(res) {
				resolve(res)
			},
			fail(err) {
				uni.showToast({
					title:"请求超时，请检查网络！",
					icon:"none"
				})
				reject(err)
			}

		})
	})
}
