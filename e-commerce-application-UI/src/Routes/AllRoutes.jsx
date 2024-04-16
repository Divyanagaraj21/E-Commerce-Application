import React from 'react'
import Home from '../Public/Home.jsx'
import Login from '../Public/Login.jsx'
import Register from '../Public/Register.jsx'
import Cart from '../Private/Customer/Cart.jsx'
import Explore from '../Private/Customer/Explore.jsx'
import AddProduct from '../Private/Seller/AddProduct.jsx'
import SellerDashboard from '../Private/Seller/SellerDashboard.jsx'
import Wishlist from '../Private/Customer/Wishlist.jsx'
import AddAddress from '../Private/Common/AddAddress.jsx'
import EditProfile from '../Private/Common/EditProfile.jsx'
import App from '../App.jsx'
import {Route,Routes} from 'react-router-dom'

const AllRoutes=() =>{
  let user={
    userId:123,
    username:"madhu",
    role:"",
    authenticated:false,
    accessExpiration: 3600,
    refreshExpiration: 1296000
  }
  const{role,authenticated}=user;
  let routes=[];
  if(authenticated)
  {
    (role=== "SELLER")?
        routes.push(
            <Route path='/seller-dashboard' element={<SellerDashboard/>} />,
            <Route path='/add-product' element={<AddProduct/>} />
        )
        : (role === "CUSTOMER")&&
        routes.push(
            <Route path='/explore' element={<Explore/>} />,
            <Route path='/cart' element={<Cart/>} />,
            <Route path='/wishlist' element={<Wishlist/>}/>
        )
    routes.push(
        <Route path='/' element={<Home/>} />,
        <Route path='/addAddress' element={<AddAddress/>} />,
        <Route path='/editProfile' element={<EditProfile/>} />,
    )
  }
  else{
    (role==="CUSTOMER")
    {
        routes.push(
            <Route path='/explore' element={<Explore/>} />,
            <Route path='/' element={<Home/>} />,
            <Route path='/login' element={<Login/>} />,
            <Route path='/register' element={<Register/>} />
        )
    }
  }
  return(
    <Routes>
        <Route path='/' element={<App/>}>{routes}</Route>
    </Routes>
  );
}

export default AllRoutes