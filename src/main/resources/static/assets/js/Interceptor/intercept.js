axios.interceptors.request.use(
  function (request) {
    // Đính token vào header mới
    if (request.method !== "get") {
      const { header, token } = getCSRFTOKEN();
      const newHeaders = {
        ...request.headers,
        [header]: token,
      };
      // override with new headers
      request.headers = newHeaders;
    }

    return request;
  },
  function (error) {
    // Xử lý lỗi
    return Promise.reject(error);
  }
);

function getCSRFTOKEN() {
  // get token protect csrf from meta tag each view thymeleaf ( layout )
  let token = document
    .querySelector("meta[name='_csrf']")
    .getAttribute("content");
  let header = document
    .querySelector("meta[name='_csrf_header']")
    .getAttribute("content");
  return { header, token };
}

axios.interceptors.response.use(
  // add X-Content-Type-Options if response don't have
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
