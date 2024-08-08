import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {useLocation} from 'react-router';
import {authenticationCheck, reIssueToken} from '../utils/authUtil';

export default function AuthInterceptor({children, roles}) {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    let checkResult = authenticationCheck(roles);

    if (checkResult === 'accessTokenIsNull') {
      navigate('/login'); // cf) navigate 는 return 처럼 요청이 완전히 끝나는게 아니기 때문에 else - if 사용 필요

    } else if (checkResult === 'accessDenied') {
      alert('접근 권한이 없습니다')
      navigate('/');

    } else if (checkResult !== 'success') {
      console.log('check - ' + checkResult)
      reIssueToken().then((reIssueSuccess) => {
        if (!reIssueSuccess) {
          navigate('/login');
        }
      });
    }

  }, [location, roles]);

  return children;
}