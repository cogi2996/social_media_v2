axios.interceptors.request.use(
  function (request) {
    // const token = getTokenInCookie();

    // Đính token vào header mới
    const newHeaders = {
      ...request.headers,
      // Authorization: token,
    };

    // Đính header mới vào lại request trước khi được gửi đi
    request = {
      ...request,
      headers: newHeaders,
    };

    return request;
  },
  function (error) {
    // Xử lý lỗi
    return Promise.reject(error);
  }
);

// function getTokenInCookie() {
//   return Cookies.get("access_token");
// }

axios.interceptors.response.use(
  function (response) {
    // Thực thi các kịch bản cần thiết ở đây
    // trước khi response đến điểm cuối

    const {
      status,
      data: { data, message },
    } = response;
    return {
      data,
      message,
      status,
    }; // ---> { "my_data": true }
  },
  function (error) {
    // Thực thi đối với các phản hồi bị lỗi
    // status code: 4xx, 5xx.
    return Promise.reject(error);
  }
);
console.log("intercept.js loaded");
