import axios from "axios";
import {jwtDecode} from "jwt-decode";

export function getCookie(name) {
  let cookies = document.cookie.split(';');
  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(name + '=')) {
      return cookie.substring(name.length + 1, cookie.length);
    }
  }
  return null;
}

export function setCookie(name, value, days) {
  let expires = "";
  if (days) {
    let date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

export function deleteCookie(name) {
  setCookie(name, "", -1);
}

export function authenticationCheck(roles) {
  let accessToken = getCookie('accessToken');

  if (!accessToken) {
    return 'accessTokenIsNull';
  }

  try {
    const decodedToken = jwtDecode(accessToken);
    const currentTime = Date.now() / 1000;

    if (decodedToken.exp < currentTime) {
      return 'tokenExpired';
    }
    if (!roles.includes(decodedToken.roles)) {
      return 'accessDenied';
    }

    return 'success';

  } catch (error) {
    return 'error';
  }
}

export async function reIssueToken() {
  const refreshToken = getCookie('refreshToken');
  if (!refreshToken) {
    return false;
  }

  let request = {
    'refreshToken': `${refreshToken}`
  }

  try {
    const url = process.env.REACT_APP_AUTH_API_HOST
    const response = await axios.post(`${url}/auth/refresh-token`, request);
    setCookie('accessToken', response.data.data.accessToken, 1);
    setCookie('refreshToken', response.data.data.refreshToken, 1);
    console.log("토큰 갱신 성공")
    return true;

  } catch (error) {
    return false;
  }
}