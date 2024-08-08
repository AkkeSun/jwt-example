import React, {useEffect, useState} from 'react';
import {apiCall} from "../utils/apiUtil";
import {useNavigate, useSearchParams} from "react-router-dom";
import {Button, Table} from "react-bootstrap";

export default function Product(prop) {

  const [searchParams, setSearchParams] = useSearchParams();
  const page = searchParams.get('page');
  const apiUrl = process.env.REACT_APP_RESOURCE_API_HOST
  const navigate = useNavigate();

  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(true);
  const [nowPage, setNowPage] = useState(page ? parseInt(page, 10) : 0);
  const [maxPage, setMaxPage] = useState(10);

  useEffect(() => {
    apiCall('get', `${apiUrl}/products?page=${nowPage}`)
    .then(res => {
      setResponse(res)
      if (res.success) {
        setMaxPage(res.data.totalPage)
      }
      setLoading(false)
    })
  }, [nowPage]); // nowPage 가 변경될 때 추가 실행 됩니다.

  if (loading) {
    return (
        <div> loading..</div>
    );
  }

  if (response.success) {
    return (
        <div className="container mt-4">
          <h3>Product List</h3>
          <Table striped bordered hover>
            <thead>
            <tr>
              <th>No</th>
              <th>Title</th>
            </tr>
            </thead>
            <tbody>
            {response.data.productList.map((product, index) => (
                <tr key={product.id}>
                  <td>{index + 1}</td>
                  <td>
                    <Button variant="link"
                            onClick={() => navigate(
                                `/products/${product.id}?from=${nowPage}`)}>
                      {product.name}
                    </Button>
                  </td>
                </tr>
            ))}
            </tbody>
          </Table>

          <div className="justify-content-md-center">
            <Button variant="outline-info"
                    onClick={() => setNowPage(nowPage > 0 ? nowPage - 1 : 0)}
                    disabled={nowPage === 0}>
              Previous Page
            </Button>
            <Button variant="outline-info"
                    onClick={() => setNowPage(nowPage + 1)}
                    disabled={nowPage >= maxPage - 1}>
              Next Page
            </Button>
          </div>
        </div>
    )
  }

  return (
      <div>
        {response.message}
      </div>
  );
}