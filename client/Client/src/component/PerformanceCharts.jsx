// src/components/BehaviorPatternChart.jsx

import React, { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer, LabelList } from 'recharts';
import axios from 'axios';

const BehaviorPatternChart = ({ childId, chartHeight = 300, fontSize = 14 }) => {
  const [data, setData] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [hoveredPattern, setHoveredPattern] = useState(null);

  useEffect(() => {
    const fetchPatterns = async () => {
      try {
        console.log('Fetching patterns for childId:', childId);
        const response = await axios.get(`/v0/patterns/${childId}`);
        console.log('Pattern data received:', response.data);
        setData(response.data);
      } catch (error) {
        console.error('Error fetching behavior pattern data:', error);
        console.error('Error details:', error.response?.data || error.message);
      }
    };

    const fetchRecommendations = async () => {
      try {
        console.log('Fetching recommendations for childId:', childId);
        console.log('ChildId type:', typeof childId);
        console.log('ChildId value:', childId);
        
        // Validate UUID format before sending
        const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
        if (!uuidRegex.test(childId)) {
          console.error('Invalid UUID format:', childId);
          setRecommendations([]);
          return;
        }
        
        const response = await axios.get(`/v0/recommendations/${childId}`);
        console.log('Recommendations received:', response.data);
        setRecommendations(response.data);
      } catch (error) {
        console.error('Error fetching recommendations:', error);
        console.error('Error details:', error.response?.data || error.message);
        console.error('Error status:', error.response?.status);
        console.error('Error URL:', error.config?.url);
        // Set empty recommendations array so chart still works
        setRecommendations([]);
      }
    };

    if (childId) {
      console.log('Chart component received childId:', childId);
      fetchPatterns();
      fetchRecommendations();
    } else {
      console.log('No childId provided to chart component');
    }
  }, [childId]);

  // Function to get recommendations for a specific pattern
  const getRecommendationsForPattern = (patternName) => {
    return recommendations.filter(rec => rec.basedOnFlag === patternName);
  };

  // Custom tooltip component
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      const patternRecommendations = getRecommendationsForPattern(label);
      
      return (
        <div className="bg-white p-4 border border-gray-300 rounded-lg shadow-lg max-w-md">
          <p className="font-semibold text-gray-800 mb-2">
            {label}: {(payload[0].value * 100).toFixed(1)}%
          </p>
          {patternRecommendations.length > 0 ? (
            <div>
              <p className="text-sm font-medium text-blue-600 mb-2">Recommendations:</p>
              {patternRecommendations.map((rec, index) => (
                <div key={index} className="mb-3 p-2 bg-blue-50 rounded">
                  <p className="text-sm font-semibold text-blue-800">{rec.title}</p>
                  <p className="text-xs text-gray-600 mt-1">{rec.description}</p>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-sm text-gray-500">
              {recommendations.length === 0 
                ? "Recommendations service is currently unavailable." 
                : "No specific recommendations available for this pattern."}
            </p>
          )}
        </div>
      );
    }
    return null;
  };

  return (
    <div className="p-4 bg-white rounded-lg shadow-md">
      <h3 className="text-lg font-semibold mb-4 text-gray-800">Behavior Pattern Confidence</h3>

      <ResponsiveContainer width="100%" height={220}>
        <BarChart
          data={data}
          margin={{ top: 10, right: 10, bottom: 20, left: 10 }}
          barSize={30}
          onMouseMove={(data) => {
            if (data && data.activeLabel) {
              setHoveredPattern(data.activeLabel);
            }
          }}
          onMouseLeave={() => setHoveredPattern(null)}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis 
            dataKey="pattern" 
            tick={{ fontSize: 12 }} 
            interval={0}
          />
          <YAxis 
            domain={[0, 1]} 
            tickFormatter={(value) => `${(value * 100).toFixed(0)}%`} 
            tick={{ fontSize: 12 }}
          />
          <Tooltip content={<CustomTooltip />} />
          <Bar dataKey="confidence" fill="#4f46e5">
            <LabelList 
              dataKey="confidence" 
              position="top" 
              formatter={(val) => `${(val * 100).toFixed(0)}%`} 
              style={{ fontSize: 12 }}
            />
          </Bar>
        </BarChart>
      </ResponsiveContainer>

      {/* Recommendations Panel */}
      {hoveredPattern && (
        <div className="mt-4 p-4 bg-blue-50 rounded-lg border border-blue-200">
          <h4 className="text-sm font-semibold text-blue-800 mb-2">
            Recommendations for: {hoveredPattern}
          </h4>
          {getRecommendationsForPattern(hoveredPattern).length > 0 ? (
            <div className="space-y-3">
              {getRecommendationsForPattern(hoveredPattern).map((rec, index) => (
                <div key={index} className="p-3 bg-white rounded border">
                  <h5 className="text-sm font-semibold text-gray-800 mb-1">{rec.title}</h5>
                  <p className="text-xs text-gray-600">{rec.description}</p>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-sm text-gray-500">
              {recommendations.length === 0 
                ? "Recommendations service is currently unavailable." 
                : "No specific recommendations available for this pattern."}
            </p>
          )}
        </div>
      )}
    </div>
  );
};

export default BehaviorPatternChart;
