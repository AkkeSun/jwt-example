import React, {useEffect, useState} from 'react';
import {useNavigate, useParams, useSearchParams} from 'react-router-dom';
import {apiCall} from "../utils/apiUtil";

export default function ProductDetail(props) {
  const navigate = useNavigate();
  const apiUrl = process.env.REACT_APP_RESOURCE_API_HOST
  const [searchParams, setSearchParams] = useSearchParams();
  const [response, setResponse] = useState([]);
  const [loading, setLoading] = useState(true);

  // router 에서 주었던 pathVariable (productId) 를 가져옵니다.
  const productId = useParams().productId

  // 입력받은 쿼리 파라미터를 조회힙나디 : get('파라미터명')
  // 주의! : 쿼리 파라미터는 항상 문자열이므로 숫자로 사용한다면 형변환이 필요합니다. : parseInt(from, 10)
  const from = searchParams.get('from');

  const goList = () => {
    if (from === null) {
      navigate("/products?page=0")
    } else {
      navigate(`/products?page=${from}`)
    }
  }

  useEffect(() => {
    apiCall('get', `${apiUrl}/products/${productId}`).then(
        res => {
          setResponse(res)
          setLoading(false)
        }
    )
  }, [productId]);

  if (loading) {
    return (
        <div> loading..</div>
    );
  }
  if (response.success) {
    return (
        <div>
          <h3>Product Detail</h3>
          id: {productId} <br/>
          name: {response.data.name} <br/>
          price: {response.data.price} <br/>
          description: {response.data.description} <br/>
          <br/>

          <button onClick={goList}>Back to Product List</button>
        </div>
    );
  }

  return (
      <div>
        {response.message}
      </div>
  )
}