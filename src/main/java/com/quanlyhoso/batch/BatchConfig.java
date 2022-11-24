package com.quanlyhoso.batch;
//package com.seino.batch;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableBatchProcessing
//public class BatchConfig {
//
//	@Autowired
//	private JobBuilderFactory jobs;
//
//	@Autowired
//	private StepBuilderFactory steps;
//
//	@Autowired
//	private ImportWBSBatch importWBS;
//
//	@Autowired
//	private ImportRedmineBatch analysisBug;
//
//	@Bean
//	public Step stepOne() {
//		return steps.get("stepOne").tasklet(importWBS).build();
//	}
//
//	@Bean
//	public Step stepTwo() {
//		return steps.get("stepTwo").tasklet(analysisBug).build();
//	}
//
//	@Bean
//	public Job TaskDaily() {
//		return jobs.get("TaskDaily").incrementer(new RunIdIncrementer()).start(stepOne()).next(stepTwo()).build();
//	}
//}