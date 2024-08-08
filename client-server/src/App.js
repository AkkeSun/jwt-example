import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Header from './component/header';
import Main from './component/main';
import Product from './component/product';
import NotFound from './component/notFound';

import 'bootstrap/dist/css/bootstrap.min.css';
import Login from "./component/login";
import AuthInterceptor from "./intercepter/authIntercepter";
import {AuthProvider} from "./context/LoginConext";
import ProductDetail from "./component/productDetail";

function App() {
  return (
      <div>
        <AuthProvider>
          <BrowserRouter>
            <Header/>
            <Routes>
              <Route path="/" element={<Main/>}/>
              <Route path="/login" element={<Login/>}/>

              <Route path="/products" element={
                <AuthInterceptor roles={['ROLE_USER', 'ROLE_ADMIN']}>
                  <Product/>
                </AuthInterceptor>
              }/>

              <Route path="/products/:productId" element={
                <AuthInterceptor roles={['ROLE_ADMIN']}>
                  <ProductDetail/>
                </AuthInterceptor>
              }/>

              <Route path="*" element={<NotFound/>}/>
            </Routes>
          </BrowserRouter>
        </AuthProvider>
      </div>
  );
}

export default App;