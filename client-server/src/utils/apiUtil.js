import axios from 'axios';
import {getCookie, reIssueToken} from "./authUtil";

export async function apiCall(method, url, data = null, retry) {

  const accessToken = getCookie('accessToken');

  function redirectLoginPage() {
    window.location.href = '/login';
  }

  const config = {
    method,
    url,
    headers: {
      'Content-Type': 'application/json',
      ...(accessToken && {'Authorization': `${accessToken}`}),
    }
  };

  if (data) {
    config.data = data;
  }

  try {

    const response = await axios(config);
    return {success: true, data: response.data.data};

  } catch (error) {

    if (error.response) {
      if (error.response.data.data.errorCode === 3001 && !retry) {

        const reIssueSuccess = await reIssueToken()
        if (reIssueSuccess) {
          return await apiCall(method, url, data, true)
        } else {
          redirectLoginPage()
        }
      }

      return {success: false, message: error.response.data.data.errorMessage};

    } else if (error.request) {
      return {success: false, message: 'No response received from server'};
    }

    return {success: false, message: error.message};
  }
}
