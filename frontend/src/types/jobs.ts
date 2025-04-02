export interface JobTitle {
  title: string;
  count: number;
}

export interface Skill {
  skill: string;
  count: number;
}

export interface DailyJobCount {
  date: string;
  count: number;
}

export interface Company {
  company: string;
  count: number;
}

export interface ExperienceLevelDistribution {
  [key: string]: number;
}

export interface JobTypeDistribution {
  [key: string]: number;
}

export interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  description: string;
  salary?: string;
  jobType: string;
  experienceLevel: string;
  datePosted: string;
  url: string;
  applicationUrl?: string;
}

export interface JobResponse {
  topJobTitles: JobTitle[];
  topSkills: Skill[];
  topCompanies: Company[];
  experienceLevelDistribution: ExperienceLevelDistribution;
  jobTypeDistribution: JobTypeDistribution;
  dailyJobCounts: DailyJobCount[];
  jobs: Job[];
} 