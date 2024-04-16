import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nameError, setNameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const handleNameChange = (e) => {
    const formattedName = e.target.value
      .trim()
      .split(' ')
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
    setName(formattedName);
    validateName(formattedName);
  };

  const validateName = (name) => {
    const namePattern = /^[a-zA-Z\s]+$/;
    if (!namePattern.test(name)) {
      setNameError('Name should contain only alphabetical characters');
    } else {
      setNameError('');
    }
  };

  const validateEmail = (email) => {
    const emailPattern = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    if (!emailPattern.test(email)) {
      setEmailError('Invalid email format');
    } else {
      setEmailError('');
    }
  };

  const validatePassword = (password) => {
    const passwordPattern = /^(?=.\d)(?=.[a-z])(?=.[A-Z])(?=.[!@#$%^&*()_+\-=\[\]{};:"\\|,.<>\/?]).{8,}$/;
    if (!passwordPattern.test(password)) {
      setPasswordError(
        'Password should be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character'
      );
    } else {
      setPasswordError('');
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    validateName(name);
    validateEmail(email);
    validatePassword(password);

    if (nameError === '' && emailError === '' && passwordError === '') {
      // Submit form data
      console.log('Form submitted successfully!');
      setName('');
      setEmail('');
      setPassword('');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100 shadow-lg">
      <div className='justify-center bg-blue-500 w-1/5 shadow-md rounded-sm'>
        <h1 className='mt-8 px pl-10 text-white text-3xl '>Looks like you're new here!</h1>
        <p className='mt-8 pl-10 pr-10 text-rose-100 text-lg'>Sign up with your mobile number to get started</p>
        <img className=" pl-12 mt-40 mb-28"
 src="/src/Images/flipkart-logo.svg" alt="images"  />
      </div>
    
      <form onSubmit={handleSubmit} className='bg-white w-1/3 shadow-md rounded-sm p-10 h-full '>
        <div className='relative mt-5'>
         
          <input autoComplete='off'
          className=" peer placeholder-transparent h-10 w-full border-b-2 focus:outline-none "
            type="text"
            id="name"
            name="name"
            value={name}
            onChange={handleNameChange}
           
          />
           <label htmlFor="name" className="absolute left-0  text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440 hover:cursor-pointer
              peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600 peer-focus:text-sm">Enter your name here</label>
          {nameError && <div className="error text-red-600 text-xs">{nameError} </div>}
        </div>

        <div className='relative mt-5'>
          
          <input
           autoComplete='off'
           className=" peer placeholder-transparent h-10 w-full border-b-2 focus:outline-none "
           type="email"
            id="email"
            name="email"
            value={email}
            onChange={(e) => {
              setEmail(e.target.value);
              validateEmail(e.target.value);
            }}
           
         
            
            required
          />
          <label htmlFor="email" className="absolute left-0  text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440 hover:cursor-pointer
              peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600 peer-focus:text-sm">Enter the gmail</label>
          {emailError && <div className="error text-xs text-red-600"> {emailError} </div>}
        </div>

        <div className='relative mt-5'>
          
          <input
           autoComplete='off'
           className=" peer placeholder-transparent h-10 w-full border-b-2 focus:outline-none "
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              validatePassword(e.target.value);
            }}
            required
          />
          <label htmlFor="password" className="absolute left-0  text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440 hover:cursor-pointer
              peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600 peer-focus:text-sm">Enter the Password:</label>
          {passwordError && <div className="error text-xs text-red-600">{passwordError} </div>}
        </div>
        <div className='mt-8 text-xs mb-5 input-focus:translate-y-20'>By continuing, you agree to Filpkart's <a className='text-blue-500' href="">Terms</a>  and <a className='text-blue-500' href="">Privicy Policy</a></div>


        <button className='bg-orange-600 rounded-xl p-3 text-white w-full mt-5'n type="submit">Submit</button>
      <Link to={'/login'}>
        <button className='bg-white border-b-2 rounded-xl p-3 text-blue-800 shadow-md font-bold w-full mt-7 mb-14'n type="submit">Existing User?Log in</button> 
        </Link>
      </form>
    </div>
  );
};

export default Register;