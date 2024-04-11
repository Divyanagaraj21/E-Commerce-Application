import React from 'react'
import Header from './Util/Header'
import {Outlet} from 'react-router-dom'

function App() {
  return (
    <div>
    <div>
      <Header/>
      <Outlet/>
    </div>
    </div>
  )
}

export default App