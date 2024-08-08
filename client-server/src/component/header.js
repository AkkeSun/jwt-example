import React, {useContext} from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Link, useNavigate} from 'react-router-dom';
import {AuthContext} from "../context/LoginConext";

const Header = () => {
  const {isLogin, logout} = useContext(AuthContext);
  const navigate = useNavigate();
  const handleLogout = () => {
    logout();
    navigate("/")
  };

  return (
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Brand> Blog </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav"/>
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">

              <Nav.Link as={Link} to='/'>Home</Nav.Link>
              <Nav.Link as={Link} to="/products">Product</Nav.Link>
              {isLogin ?
                  (<Nav.Link onClick={handleLogout}>Logout</Nav.Link>) :
                  (<Nav.Link as={Link} to="/login">Login</Nav.Link>)}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
  );
}

export default Header;