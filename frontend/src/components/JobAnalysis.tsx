import React, { useState } from 'react';
import { JobTitle, Skill, Company, JobResponse } from '../types/jobs';
import JobCountChart from './JobCountChart';

interface JobAnalysisProps {
  data: JobResponse;
}

type ListItem = JobTitle | Skill | Company | { [key: string]: string | number };

const JobAnalysis: React.FC<JobAnalysisProps> = ({ data }) => {
  const [expandedSection, setExpandedSection] = useState<string | null>(null);
  const ITEMS_TO_SHOW = 5;

  const toggleSection = (section: string) => {
    setExpandedSection(expandedSection === section ? null : section);
  };

  const renderList = (items: ListItem[], keyField: string, valueField: string, section: string) => {
    const isExpanded = expandedSection === section;
    const displayItems = isExpanded ? items : items.slice(0, ITEMS_TO_SHOW);
    const hasMore = items.length > ITEMS_TO_SHOW;

    return (
      <div className="space-y-2">
        {displayItems.map((item: ListItem, index: number) => (
          <div key={index} className="flex justify-between items-center text-sm">
            <span className="text-soft-dark truncate">{String(item[keyField as keyof ListItem])}</span>
            <span className="text-pastel-blue font-medium ml-2">{String(item[valueField as keyof ListItem])}</span>
          </div>
        ))}
        {hasMore && (
          <button
            onClick={() => toggleSection(section)}
            className="text-pastel-purple text-sm font-medium hover:text-pastel-purple/80 transition-colors duration-200"
          >
            {isExpanded ? 'Show Less' : `Show ${items.length - ITEMS_TO_SHOW} More`}
          </button>
        )}
      </div>
    );
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {/* Top Job Titles */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-blue" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
          Top Job Titles
        </h3>
        {renderList(data.topJobTitles, 'title', 'count', 'titles')}
      </div>

      {/* Top Skills */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-purple" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
          </svg>
          Top Skills
        </h3>
        {renderList(data.topSkills, 'skill', 'count', 'skills')}
      </div>

      {/* Top Companies */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-pink" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
          </svg>
          Top Companies
        </h3>
        {renderList(data.topCompanies, 'company', 'count', 'companies')}
      </div>

      {/* Experience Level Distribution */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-blue" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          Experience Levels
        </h3>
        {renderList(
          Object.entries(data.experienceLevelDistribution).map(([level, count]) => ({ level, count })),
          'level',
          'count',
          'experience'
        )}
      </div>

      {/* Job Type Distribution */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-purple" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          Job Types
        </h3>
        {renderList(
          Object.entries(data.jobTypeDistribution).map(([type, count]) => ({ type, count })),
          'type',
          'count',
          'types'
        )}
      </div>

      {/* Daily Job Counts */}
      <div className="bg-soft-gray rounded-xl shadow-neumorphic p-4 lg:col-span-3">
        <h3 className="text-lg font-semibold text-soft-dark mb-3 flex items-center">
          <svg className="w-5 h-5 mr-2 text-pastel-pink" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          Daily Job Counts
        </h3>
        <JobCountChart data={data.dailyJobCounts} />
      </div>
    </div>
  );
};

export default JobAnalysis; 