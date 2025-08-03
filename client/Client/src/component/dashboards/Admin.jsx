import React, { useState, useEffect } from 'react';
import axios from 'axios';
import LogoutButton from '../Logout';

const AdminDashboard = () => {
  // Define allowed standards, matching the backend enum
  const STANDARDS = [
    'FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH',
    'SIXTH', 'SEVENTH', 'EIGHTH', 'NINTH'
  ];

  // State for fetching and displaying faculty assignments
  const [assignments, setAssignments] = useState([]);
  const [loadingAssignments, setLoadingAssignments] = useState(true);
  const [errorAssignments, setErrorAssignments] = useState('');

  // State for the new assignment form
  const [newAssignment, setNewAssignment] = useState({
    facultyEmail: '',
    standard: '',
  });
  const [isAssigning, setIsAssigning] = useState(false);
  const [assignmentSuccess, setAssignmentSuccess] = useState('');
  const [assignmentError, setAssignmentError] = useState('');

  // Function to fetch all faculty assignments
  const fetchAssignments = async () => {
    setLoadingAssignments(true);
    setErrorAssignments('');
    try {
      const response = await axios.get('/v0/admin/faculty-assignments');
      setAssignments(response.data);
    } catch (err) {
      console.error('Error fetching assignments:', err);
      setErrorAssignments('Failed to fetch assignments. Please try again.');
    } finally {
      setLoadingAssignments(false);
    }
  };

  // Fetch assignments on initial component load
  useEffect(() => {
    fetchAssignments();
  }, []);

  // Handle form input changes for the new assignment
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewAssignment(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Handle submission of the new assignment form
  const handleAssignmentSubmit = async (e) => {
    e.preventDefault();
    setAssignmentSuccess('');
    setAssignmentError('');
    setIsAssigning(true);

    if (!newAssignment.facultyEmail || !newAssignment.standard) {
      setAssignmentError('Please fill in both the faculty email and standard.');
      setIsAssigning(false);
      return;
    }
    
    try {
      // POST request to assign a faculty to a standard
      const response = await axios.post('/v0/admin/faculty-assignments', newAssignment);
      setAssignmentSuccess('Faculty successfully assigned to standard!');
      
      // Clear the form and refresh the assignments list
      setNewAssignment({
        facultyEmail: '',
        standard: '',
      });
      fetchAssignments();
    } catch (err) {
      console.error('Error assigning faculty:', err);
      setAssignmentError('Failed to assign faculty. Please check the details and try again.');
    } finally {
      setIsAssigning(false);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-3xl font-bold text-blue-800 mb-6">Admin Dashboard</h2>
        <LogoutButton />
      {/* Faculty Assignment Form */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h3 className="text-xl font-semibold text-blue-700 mb-4">Assign Faculty to a Standard</h3>
        <form onSubmit={handleAssignmentSubmit} className="space-y-4">
          <div>
            <label htmlFor="facultyEmail" className="block text-gray-700 text-sm font-bold mb-2">
              Faculty Email:
            </label>
            <input
              type="email"
              id="facultyEmail"
              name="facultyEmail"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newAssignment.facultyEmail}
              onChange={handleInputChange}
              placeholder="faculty@example.com"
              required
            />
          </div>
          <div>
            <label htmlFor="standard" className="block text-gray-700 text-sm font-bold mb-2">
              Standard:
            </label>
            <select
              id="standard"
              name="standard"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newAssignment.standard}
              onChange={handleInputChange}
              required
            >
              <option value="">Select a Standard</option>
              {STANDARDS.map(std => (
                <option key={std} value={std}>{std}</option>
              ))}
            </select>
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            disabled={isAssigning}
          >
            {isAssigning ? 'Assigning...' : 'Assign Faculty'}
          </button>
          {assignmentSuccess && <p className="text-green-500 text-sm mt-2">{assignmentSuccess}</p>}
          {assignmentError && <p className="text-red-500 text-sm mt-2">{assignmentError}</p>}
        </form>
      </div>

      {/* Current Faculty Assignments List */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h3 className="text-xl font-semibold text-blue-700 mb-4">Current Faculty Assignments</h3>
        {loadingAssignments && <div className="text-gray-500">Loading assignments...</div>}
        {errorAssignments && <div className="text-red-500 font-semibold">{errorAssignments}</div>}
        
        {!loadingAssignments && !errorAssignments && (
          assignments.length > 0 ? (
            <ul className="divide-y divide-gray-200">
              {assignments.map((assignment, index) => (
                <li key={index} className="py-4">
                  <p className="text-gray-700">
                    <strong>Faculty:</strong> {assignment.facultyEmail}
                  </p>
                  <p className="text-gray-700">
                    <strong>Standard:</strong> {assignment.standard}
                  </p>
                </li>
              ))}
            </ul>
          ) : (
            <div className="text-gray-500">No faculty assignments found.</div>
          )
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;