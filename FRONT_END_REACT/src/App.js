import logo from './logo.svg';
import './App.css';
import Routes from "./Routes";


function App() {
  return (
    <div className="App">
      
      <a href="/">Polls</a>
      <a href="/login">Log in</a>

      <Routes />
    </div>
  );
}

export default App;
