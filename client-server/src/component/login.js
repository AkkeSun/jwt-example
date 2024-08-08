import React, {useContext, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import {Button} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import {apiCall} from "../utils/apiUtil";
import {setCookie} from "../utils/authUtil";
import {AuthContext} from "../context/LoginConext";

const AddProduct = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  const authApiUrl = process.env.REACT_APP_AUTH_API_HOST
  const {setLogin} = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validation()) {
      return;
    }

    const request = {
      username,
      password
    };

    const response = await apiCall('post', `${authApiUrl}/auth/token`, request)
    if (response.success) {
      setCookie('accessToken', response.data.accessToken, 1);
      setCookie('refreshToken', response.data.refreshToken, 1);
      setLogin(true)
      navigate('/');
    } else {
      setMessage(response.message);
    }
  }

  const validation = () => {
    if (!username) {
      setMessage('이름은 필수값 입니다.');
      return false;
    } else if (!password) {
      setMessage('비밀번호는 필수값 입니다.');
      return false;
    }
    return true;
  }

  return (
      <Container>
        <br/>
        <h2>Login Page</h2>
        {message && <p>{message}</p>}

        <Form className="mt-4" onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Control type="text" placeholder="username"
                          onChange={(e) => setUsername(e.target.value)}/>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Control type="password" placeholder="password"
                          onChange={(e) => setPassword(e.target.value)}/>
          </Form.Group>
          <Button variant="outline-info" type="submit">login</Button>
        </Form>
      </Container>
  );
};

export default AddProduct;