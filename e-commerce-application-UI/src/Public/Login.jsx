import React from 'react'
import { Link } from 'react-router-dom'

function Login() {



  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100 shadow-lg">
      <div className='justify-center bg-blue-500 w-1/5 shadow-md rounded-sm'>
        <h1 className='mt-8 px pl-10 text-white text-3xl '>Login</h1>
        <p className='mt-8 pl-10 text-rose-100 text-lg'>Get access to your Orders, Wishlist and Recommendations</p>
        <img className=" pl-12 mt-40 mb-28"
 src="/src/Images/flipkart-logo.svg" alt="images"  />
      </div>
      <div className=' bg-white w-1/3 shadow-md rounded-sm p-10 h-full '>
        
        <div className="relative mt-5" >
							<input autocomplete="off" id="password" name="password" type="" className=" peer placeholder-transparent h-10 w-full border-b-2    focus:outline-none " placeholder="Password" />
							<label for="password" className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440 hover:cursor-pointer
              peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600 peer-focus:text-sm">Enter Email/Mobile number</label>
						</div>
        <div className='mt-8 text-xs mb-5 input-focus:translate-y-20'>By continuing, you agree to Filpkart's <a className='text-blue-500' href="">Terms</a>  and <a className='text-blue-500' href="">Privicy Policy</a></div>
        <button className='bg-orange-600 rounded-xl p-3 text-white w-full mb-48'>Request OTP</button>
        <div className=' flex justify-center w-full'>
        <Link className='text-blue-500 justify-center mt-6' to={'/register'}>New To Flipkart? Create a account</Link>
        </div>

      </div>
    </div>
  )
}
   

export default Login