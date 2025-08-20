import React, { useState, useEffect } from 'react';

function Subscription({ onSubscriptionChange }) {
  const [symbol, setSymbol] = useState('');
  const [subscriptions, setSubscriptions] = useState([]);

  useEffect(() => {
    fetchSubscriptions();
  }, []);

  const fetchSubscriptions = async () => {
    try {
      const response = await fetch('/stocks');
      const data = await response.json();
      setSubscriptions(data);
    } catch (error) {
      console.error('Error fetching subscriptions:', error);
    }
  };

  const handleAddSubscription = async (e) => {
    e.preventDefault();
    if (!symbol) return;

    try {
      await fetch('/stocks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ symbol }),
      });
      setSymbol('');
      fetchSubscriptions();
    } catch (error) {
      console.error('Error adding subscription:', error);
    }
  };

  useEffect(() => {
    onSubscriptionChange(subscriptions);
  }, [subscriptions, onSubscriptionChange]);

  return (
    <div className="subscription-container">
      <form onSubmit={handleAddSubscription}>
        <input
          type="text"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
          placeholder="Enter stock symbol"
        />
        <button type="submit">Add Subscription</button>
      </form>
      <div className="subscription-list">
        <h3>Subscriptions:</h3>
        <ul>
          {subscriptions.map((sub) => (
            <li key={sub}>{sub}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default Subscription;
