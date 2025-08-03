import React, { useState } from 'react';
import axios from 'axios';
import LogoutButton from '../Logout'; // Reusing the LogoutButton component
import BehaviorPatternChart from '../PerformanceCharts';

const TeacherDashboard = () => {
  // State for fetching students of a specific class
  const [classToFetch, setClassToFetch] = useState('');
  const [fetchedStudents, setFetchedStudents] = useState([]);
  const [fetchError, setFetchError] = useState('');
  const [isFetching, setIsFetching] = useState(false);
  const [expandedStudentId, setExpandedStudentId] = useState(null);

  // State for adding a new child
  const [newChild, setNewChild] = useState({
    fullName: '',
    dateOfBirth: '', // YYYY-MM-DD format for LocalDate
    gender: '',
    guardianEmail: '',
    standard: '',
  });
  const [addSuccess, setAddSuccess] = useState('');
  const [addError, setAddError] = useState('');
  const [isAdding, setIsAdding] = useState(false);

  // State for selecting a student and adding a comment
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [eventContext, setEventContext] = useState('');
  const [rawText, setRawText] = useState('');
  const [isCommenting, setIsCommenting] = useState(false);
  const [commentSuccess, setCommentSuccess] = useState('');
  const [commentError, setCommentError] = useState('');

  // Define allowed standards
  const STANDARDS = [
    'FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH',
    'SIXTH', 'SEVENTH', 'EIGHTH', 'NINTH'
  ];

  // Handler to fetch all students for a specific class
  const handleFetchStudents = async (e) => {
    e.preventDefault();
    setFetchedStudents([]);
    setSelectedStudent(null); // Deselect any student
    setFetchError('');
    setIsFetching(true);

    if (!classToFetch || isNaN(parseInt(classToFetch))) {
      setFetchError('Please enter a valid class number (1-9).');
      setIsFetching(false);
      return;
    }
    
    const standardIndex = parseInt(classToFetch) - 1;
    if (standardIndex < 0 || standardIndex >= STANDARDS.length) {
      setFetchError('Invalid class number. Please enter a number between 1 and 9.');
      setIsFetching(false);
      return;
    }

    const standardName = STANDARDS[standardIndex];

    try {
      // Assuming a backend endpoint like `/v0/faculty/children/{standardName}`
      const response = await axios.get(`/v0/faculty/children/${standardName}`);
      console.log('Fetched students:', response.data)
      setFetchedStudents(response.data);
    } catch (error) {
      console.error('Error fetching student list:', error);
      setFetchError('Failed to fetch students. Please check the class and try again.');
    } finally {
      setIsFetching(false);
    }
  };

  // Handler to select a student from the list
  const handleSelectStudent = (student) => {
    console.log('Selected student:', student);
    console.log('Student keys:', Object.keys(student));
    setSelectedStudent(student);
    setEventContext(''); // Clear previous event context
    setRawText(''); // Clear previous comment text
    setCommentSuccess(''); // Clear previous success message
    setCommentError(''); // Clear previous error message
  };

  // Handler to submit a comment for the selected student
  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    setCommentSuccess('');
    setCommentError('');
    setIsCommenting(true);

    if (!eventContext || !rawText) {
      setCommentError('Please enter both event context and comment text before submitting.');
      setIsCommenting(false);
      return;
    }

    if (!selectedStudent) {
      setCommentError('No student is selected.');
      setIsCommenting(false);
      return;
    }

    // Check for common ID field names
    const studentId = selectedStudent.UUID || selectedStudent.id || selectedStudent.uuid || selectedStudent.childId;
    if (!studentId) {
      console.error('Student object:', selectedStudent);
      setCommentError('Student data is missing ID field. Available fields: ' + Object.keys(selectedStudent).join(', '));
      setIsCommenting(false);
      return;
    }

    try {
      // The backend needs the student's ID to link the comment
      const commentData = {
        childId: studentId,
        eventContext: eventContext,
        rawText: rawText,
      };

      // Assuming a backend endpoint for adding a comment
      console.log(commentData.childId)
      const response = await axios.post('/v0/faculty/behavior-log', commentData);

      setCommentSuccess('Comment added successfully!');
      setEventContext(''); // Clear event context after successful submission
      setRawText(''); // Clear raw text after successful submission
      console.log('Comment submitted:', response.data);
    } catch (error) {
      console.error('Error submitting comment:', error);
      setCommentError('Failed to submit comment. Please try again.');
    } finally {
      setIsCommenting(false);
    }
  };


  // Handler for new child form input changes
  const handleChildInputChange = (e) => {
    const { name, value } = e.target;
    setNewChild(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Handler for adding new child details
  const handleAddChild = async (e) => {
    e.preventDefault();
    setAddSuccess('');
    setAddError('');
    setIsAdding(true);

    // Basic validation
    if (!newChild.fullName || !newChild.dateOfBirth || !newChild.gender || !newChild.guardianEmail || !newChild.standard) {
      setAddError('Please fill in all fields for the new child.');
      setIsAdding(false);
      return;
    }

    if (!STANDARDS.includes(newChild.standard.toUpperCase())) {
      setAddError('Invalid standard. Please select from the allowed options.');
      setIsAdding(false);
      return;
    }

    try {
      const response = await axios.post('/v0/faculty/children', newChild);
      setAddSuccess('Child details added successfully!');
      setNewChild({ // Reset form fields
        fullName: '',
        dateOfBirth: '',
        gender: '',
        guardianEmail: '',
        standard: '',
      });
      console.log('Child added:', response.data);
    } catch (error) {
      console.error('Error adding child details:', error);
      setAddError('Failed to add child details. Please try again.');
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-blue-800">Teacher Dashboard</h2>
        <LogoutButton />
      </div>

      {/* Add Child Details Section */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h3 className="text-xl font-semibold text-blue-700 mb-4">Add New Child Details</h3>
        <form onSubmit={handleAddChild} className="space-y-4">
          <div>
            <label htmlFor="fullName" className="block text-gray-700 text-sm font-bold mb-2">
              Full Name:
            </label>
            <input
              type="text"
              id="fullName"
              name="fullName"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newChild.fullName}
              onChange={handleChildInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="dateOfBirth" className="block text-gray-700 text-sm font-bold mb-2">
              Date of Birth:
            </label>
            <input
              type="date"
              id="dateOfBirth"
              name="dateOfBirth"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newChild.dateOfBirth}
              onChange={handleChildInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="gender" className="block text-gray-700 text-sm font-bold mb-2">
              Gender:
            </label>
            <select
              id="gender"
              name="gender"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newChild.gender}
              onChange={handleChildInputChange}
              required
            >
              <option value="">Select Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>
          <div>
            <label htmlFor="guardianEmail" className="block text-gray-700 text-sm font-bold mb-2">
              Guardian Email:
            </label>
            <input
              type="email"
              id="guardianEmail"
              name="guardianEmail"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={newChild.guardianEmail}
              onChange={handleChildInputChange}
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
              value={newChild.standard}
              onChange={handleChildInputChange}
              required
            >
              <option value="">Select Standard</option>
              {STANDARDS.map(std => (
                <option key={std} value={std}>{std}</option>
              ))}
            </select>
          </div>
          <button
            type="submit"
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            disabled={isAdding}
          >
            {isAdding ? 'Adding...' : 'Add Child'}
          </button>
          {addSuccess && <p className="text-green-500 text-sm mt-2">{addSuccess}</p>}
          {addError && <p className="text-red-500 text-sm mt-2">{addError}</p>}
        </form>
      </div>

      {/* Fetch Students Section */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h3 className="text-xl font-semibold text-blue-700 mb-4">Fetch Students by Class</h3>
        <form onSubmit={handleFetchStudents} className="space-y-4">
          <div>
            <label htmlFor="classToFetch" className="block text-gray-700 text-sm font-bold mb-2">
              Class:
            </label>
            <input
              type="text"
              id="classToFetch"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={classToFetch}
              onChange={(e) => setClassToFetch(e.target.value)}
              placeholder="e.g., 5"
            />
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            disabled={isFetching}
          >
            {isFetching ? 'Fetching...' : 'Fetch Students'}
          </button>
          {fetchError && <p className="text-red-500 text-sm mt-2">{fetchError}</p>}
        </form>

        {fetchedStudents.length > 0 && (
          <div className="mt-6">
            <h4 className="text-lg font-medium text-blue-800">Students in Class {classToFetch}:</h4>
            <ul className="mt-2 space-y-2">
              
              {fetchedStudents.map((student) => {
                const studentId = student.UUID || student.id || student.uuid || student.childId;
                return (
                <li
                  key={studentId}
                  onClick={() => handleSelectStudent(student)}
                  className={`p-3 border rounded-lg cursor-pointer transition-colors
                  ${selectedStudent && (selectedStudent.UUID || selectedStudent.id || selectedStudent.uuid || selectedStudent.childId) === studentId ? 'bg-blue-200 border-blue-500' : 'bg-gray-100 hover:bg-gray-200'}`}
                >
                  <p className="font-semibold">{student.fullName}</p>
                  <p className="text-sm text-gray-600">Standard: {student.standard}</p>
                  <p className="text-xs text-gray-500">Guardian: {student.guardianEmail}</p>
                </li>
                );
              })}
            </ul>
          </div>
        )}
      </div>

      {/* Commenting Section */}
      {/* {selectedStudent && (
        <div className="mt-8 bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
          <h3 className="text-xl font-semibold text-blue-700 mb-4">
            Comment on {selectedStudent.fullName}
          </h3>
          <form onSubmit={handleCommentSubmit} className="space-y-4">
            <div>
              <label htmlFor="comment1" className="block text-gray-700 text-sm font-bold mb-2">
                Event Context:
              </label>
              <textarea
                id='comment1'
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                rows="4"
                value={eventContext}
                onChange={(e) => setEventContext(e.target.value)}
                required
              ></textarea>
              <label htmlFor="comment" className="block text-gray-700 text-sm font-bold mb-2">
                Comment text:
              </label>
              <textarea
                id="comment"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                rows="4"
                value={rawText}
                onChange={(e) => setRawText(e.target.value)}
                required
              ></textarea>
            </div>
            <button
              type="submit"
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              disabled={isCommenting}
            >
              {isCommenting ? 'Submitting...' : 'Submit Comment'}
            </button>
            {commentSuccess && <p className="text-green-500 text-sm mt-2">{commentSuccess}</p>}
            {commentError && <p className="text-red-500 text-sm mt-2">{commentError}</p>}
          </form>
        </div>
      )} */}

      {selectedStudent && (
  <div className="mt-10 grid grid-cols-1 lg:grid-cols-2 gap-6 items-start">
    {/* Comment Form */}
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h3 className="text-xl font-semibold text-green-800 mb-4">
        Add a Comment for {
          selectedStudent?.fullName || 'Selected Child'
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
            rows="3"
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
            rows="3"
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

         {/* Bar Chart */}
     <div className="bg-white p-6 rounded-lg shadow-md">
       <h3 className="text-xl font-semibold text-green-800 mb-4">Behavioral Insights</h3>
       <BehaviorPatternChart 
         childId={selectedStudent.UUID || selectedStudent.id || selectedStudent.uuid || selectedStudent.childId} 
         chartHeight={220} 
         fontSize={12} 
       />
     </div>
  </div>
)}

    </div>
  );
};

export default TeacherDashboard;