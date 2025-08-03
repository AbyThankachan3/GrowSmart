import React, { useState, useEffect } from 'react';
import axios from 'axios';
import LogoutButton from '../Logout';
import BehaviorPatternChart from '../PerformanceCharts';

const ParentDashboard = ({childId}) => {
  const [children, setChildren] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [selectedChildId, setSelectedChildId] = useState(null);
  const [eventContext, setEventContext] = useState('');
  const [rawText, setRawText] = useState('');
  const [isCommenting, setIsCommenting] = useState(false);
  const [commentSuccess, setCommentSuccess] = useState('');
  const [commentError, setCommentError] = useState('');

  useEffect(() => {
    const fetchChildData = async () => {
      try {
        const response = await axios.get('/v0/parents/child');
        setChildren(response.data); // assuming this is an array
      } catch (err) {
        console.error('Error fetching child data:', err);
        setError('Failed to fetch child data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchChildData();
  }, []);

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    setCommentSuccess('');
    setCommentError('');
    setIsCommenting(true);

    if (!eventContext || !rawText) {
      setCommentError('Please fill out both fields before submitting.');
      setIsCommenting(false);
      return;
    }

    const child = children.find(c => c.childId === selectedChildId);
    if (!child) {
      setCommentError('Selected child data is not available.');
      setIsCommenting(false);
      return;
    }

    const commentData = {
      childId: child.childId,
      eventContext,
      rawText,
    };

    try {
      const response = await axios.post('/v0/faculty/behavior-log', commentData);
      setCommentSuccess('Comment added successfully!');
      setEventContext('');
      setRawText('');
    } catch (err) {
      console.error('Error submitting comment:', err);
      setCommentError('Failed to submit comment. Please try again.');
    } finally {
      setIsCommenting(false);
    }
  };

  return (
    <div className="p-9 bg-gray-50 min-h-screen">
      {/* Header */}
      <div className='flex items-center justify-between w-full mb-6'>
        <h2 className="text-3xl font-bold text-green-700 text-center flex-grow">
          Parent Dashboard
        </h2>
        <LogoutButton />
      </div>

      {/* Loading and error state */}
      {loading && <p className="text-gray-500">Loading child details...</p>}
      {error && <p className="text-red-500 font-semibold">{error}</p>}

      {/* Child list */}
      <div className="grid gap-4 md:grid-cols-2">
        {children.map((child) => (
          <div key={child.childId}>
  <div
    className={`bg-white p-6 rounded-lg shadow-md cursor-pointer ${
      selectedChildId === child.childId ? 'border-2 border-green-500' : ''
    }`}
    onClick={() =>
      setSelectedChildId(
        selectedChildId === child.childId ? null : child.childId
      )
    }
  >
    <h3 className="text-xl font-semibold text-green-800 mb-2">
      {child.fullName}
    </h3>
    <p className="text-gray-700"><strong>Standard:</strong> {child.standard}</p>
    <p className="text-gray-700"><strong>Date of Birth:</strong> {child.dateOfBirth}</p>
  </div>

  {selectedChildId === child.childId && (
    <div className="mt-4 mb-6">
      <BehaviorPatternChart childId={child.childId} />
    </div>
  )}
</div>
        ))}
      </div>
      

      {/* Comment form */}
      {selectedChildId && (
        <div className="mt-8 bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
          <h3 className="text-xl font-semibold text-green-800 mb-4">
            Add a Comment for {
              children.find(c => c.childId === selectedChildId)?.fullName || 'Selected Child'
            }
          </h3>
          <form onSubmit={handleCommentSubmit} className="space-y-4">
            <div>
              <label htmlFor="eventContext" className="block text-sm font-bold mb-2">
                Event Context:
              </label>
              <textarea
                id="eventContext"
                className="w-full border rounded px-3 py-2"
                rows="4"
                value={eventContext}
                onChange={(e) => setEventContext(e.target.value)}
                required
              ></textarea>
            </div>
            <div>
              <label htmlFor="rawText" className="block text-sm font-bold mb-2">
                Comment Text:
              </label>
              <textarea
                id="rawText"
                className="w-full border rounded px-3 py-2"
                rows="4"
                value={rawText}
                onChange={(e) => setRawText(e.target.value)}
                required
              ></textarea>
            </div>
            <button
              type="submit"
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
              disabled={isCommenting}
            >
              {isCommenting ? 'Submitting...' : 'Submit Comment'}
            </button>
            {commentSuccess && <p className="text-green-500 text-sm mt-2">{commentSuccess}</p>}
            {commentError && <p className="text-red-500 text-sm mt-2">{commentError}</p>}
          </form>
        </div>
      )}

      {!loading && children.length === 0 && !error && (
        <p className="text-gray-500">No child data found.</p>
      )}
    </div>
  );
};

export default ParentDashboard;
