import React, { useState, useEffect } from 'react';
import protobuf from 'protobufjs';
import './App.css';

const protoPath = 'ticker.proto';

function App() {
  const [tickers, setTickers] = useState([]);
  const [tickerProto, setTickerProto] = useState(null);

  useEffect(() => {
    async function loadProto() {
      try {
        const root = await protobuf.load(protoPath);
        const Ticker = root.lookupType('Ticker');
        setTickerProto({ Ticker });
      } catch (error) {
        console.error('Error loading proto file:', error);
      }
    }
    loadProto();
  }, []);

  useEffect(() => {
    if (!tickerProto) return;

    const ws = new WebSocket(`ws://${window.location.host}/ws`);
    ws.binaryType = 'arraybuffer';

    ws.onopen = () => {
      console.log('WebSocket connected');
      ws.send('["BTC-USD","ETH-USD","EUR=X"]');
    };

    ws.onmessage = (event) => {
      try {
        const buffer = new Uint8Array(event.data);
        const decoded = tickerProto.Ticker.decode(buffer);
        const newTicker = tickerProto.Ticker.toObject(decoded, {
          longs: String,
          enums: String,
          bytes: String,
        });

        setTickers((prevTickers) => {
          const existingTickerIndex = prevTickers.findIndex((t) => t.id === newTicker.id);
          if (existingTickerIndex !== -1) {
            const updatedTickers = [...prevTickers];
            updatedTickers[existingTickerIndex] = newTicker;
            return updatedTickers;
          } else {
            return [...prevTickers, newTicker];
          }
        });
      } catch (error) {
        console.error('Error decoding message:', error);
      }
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
    };

    ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    return () => {
      ws.close();
    };
  }, [tickerProto]);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Yahoo Finance Streamer</h1>
      </header>
      <div className="ticker-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Price</th>
              <th>Change</th>
              <th>Change %</th>
              <th>Day Volume</th>
            </tr>
          </thead>
          <tbody>
            {tickers.map((ticker) => (
              <tr key={ticker.id}>
                <td>{ticker.id}</td>
                <td>{ticker.price.toFixed(2)}</td>
                <td style={{ color: ticker.change < 0 ? 'red' : 'green' }}>
                  {ticker.change.toFixed(2)}
                </td>
                <td style={{ color: ticker.changePercent < 0 ? 'red' : 'green' }}>
                  {ticker.changePercent.toFixed(2)}%
                </td>
                <td>{ticker.dayVolume}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default App;
