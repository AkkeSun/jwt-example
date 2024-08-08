import React, {createContext, useEffect, useState} from 'react';
import {deleteCookie, getCookie} from '../utils/authUtil';

/*
  Context
  컴포넌트끼리 값을 주고받을 때 props 로 주고받는것이 아니라
  전역적으로 주고받기를 원할 때 사용
 */
export const AuthContext = createContext({}); //context 선언

export const AuthProvider = ({children}) => {
  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    const token = getCookie('accessToken');
    if (token) {
      setIsLogin(true)
    } else {
      setIsLogin(false)
    }
  }, []);

  const logout = () => {
    deleteCookie('accessToken');
    deleteCookie('refreshToken');
    setIsLogin(false);
  };

  const setLogin = (result) => {
    setIsLogin(result);
  };

  return (
      <AuthContext.Provider value={{isLogin, logout, setLogin}}>
        {children}
      </AuthContext.Provider>
  );
};
