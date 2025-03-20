const API_URL = 'http://localhost:8080/api/auth';
const BOOKMARK_API_URL = 'http://localhost:8080/customer/api/bookmarks';

Kakao.init('b5d8b9f0f40b2d55397786378ae4b1a4');

const urlParams = new URLSearchParams(window.location.search);
if (urlParams.get("error") === "unauthorized") {
    alert("로그인이 필요합니다.");
    window.location.href = "/login";
}

function showLoginForm() {
    document.getElementById('signup-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('social-signup-form').style.display = 'none';
}

function showSignupForm() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('signup-form').style.display = 'block';
    document.getElementById('social-signup-form').style.display = 'none';
}

function showSocialSignupForm() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('signup-form').style.display = 'none';
    document.getElementById('social-signup-form').style.display = 'block';
}

async function signup() {
    const email = document.getElementById('signup-email').value;
    const password = document.getElementById('signup-password').value;
    const name = document.getElementById('signup-name').value;
    const nickname = document.getElementById('signup-nickname').value;
    const phoneNumber = document.getElementById('signup-phone').value;
    const userRole = document.getElementById('signup-role').value;

    const data = {
        email: email,
        password: password,
        name: name,
        nickname: nickname,
        phoneNumber: phoneNumber,
        userRole: userRole
    };

    try {
        const response = await fetch(API_URL + '/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('회원가입 성공!');
            showLoginForm();
        } else {
            const error = await response.json();
            alert(error.errorMessage);
        }
    } catch (error) {
        alert('알 수 없는 에러: ' + error);
    }
}

async function login() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    const data = {
        email: email,
        password: password
    };

    try {
        const response = await fetch(API_URL + '/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const responseData = await response.json();
            const accessToken = responseData.accessToken;
            const refreshToken = responseData.refreshToken;
            const accessTokenExpiryTime = responseData.accessTokenExpiryTime;

            console.log(accessToken);
            console.log(refreshToken);
            console.log(accessTokenExpiryTime)
            console.log(!accessToken);

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('accessTokenExpiryTime', accessTokenExpiryTime);

            alert('로그인 성공!');
            window.location.href = '/';
        } else {
            const error = await response.json();
            console.log(error)
            alert(error.errorMessage);
        }
    } catch (error) {
        alert('알 수 없는 에러: ' + error);
    }
}

function kakaoLogin() {
    Kakao.Auth.login({
        success: function(authObj) {
            const accessToken = authObj.access_token;
            console.log(authObj)
            sendKakaoLoginToBackend(accessToken);
        },
        fail: function(err) {
            alert('카카오 로그인 실패: ' + JSON.stringify(err));
        }
    });

    // Kakao.Auth.login({
    //     success: function (authObj) {
    //         Kakao.API.request({
    //             url: '/v2/user/me',
    //             success: function (res) {
    //                 const kakaoCode = res.id;
    //                 console.log(res);
    //                 sendKakaoLoginToBackend(kakaoCode);
    //             },
    //             fail: function (error) {
    //                 alert('카카오 사용자 정보 요청 실패: ' + JSON.stringify(error));
    //             }
    //         });
    //     },
    //     fail: function (err) {
    //         alert('카카오 로그인 실패: ' + JSON.stringify(err));
    //     }
    // });
}

async function sendKakaoLoginToBackend(accessToken) {
    try {
        const response = await fetch('/api/auth/kakao/callback?access_token=' + accessToken, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (response.ok) {
            const responseData = await response.json();
            const accessToken = responseData.accessToken;
            const refreshToken = responseData.refreshToken;
            const accessTokenExpiryTime = responseData.accessTokenExpiryTime;

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('accessTokenExpiryTime', accessTokenExpiryTime);

            alert('카카오 로그인 성공!');
            window.location.href = '/';
        } else {
            const error = await response.json();
            alert(error.errorMessage);
        }
    } catch (error) {
        alert('알 수 없는 에러: ' + error);
    }
}

async function submitSocialSignup() {
    const phoneNumber = document.getElementById('social-phone').value;
    const verificationCode = document.getElementById('social-verification-code').value;
    alert('추가 정보 제출!');
}