package com.assertramdom.poc.batch.config;

import com.assertramdom.poc.batch.domain.Furniture;
import com.assertramdom.poc.batch.processor.FurnitureItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * Created on 09/05/15.
 */

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;



    @Bean
    public ItemReader<Furniture> reader() {
        FlatFileItemReader<Furniture> reader = new FlatFileItemReader<Furniture>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Furniture>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name", "cost" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Furniture>() {{
                setTargetType(Furniture.class);
            }});
        }});
        return  reader;
    }

    @Bean
    public ItemProcessor<Furniture, Furniture> processor() {
        return  new FurnitureItemProcessor();
    }

    @Bean
    public ItemWriter<Furniture> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Furniture> writer = new JdbcBatchItemWriter<Furniture>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Furniture>());
        writer.setSql("INSERT INTO furniture (name, cost) VALUES (:name, :cost)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    public Job importFurnitureJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importFurtnitureJob").incrementer(new RunIdIncrementer()).flow(s1).end().build();
    }

    @Bean
    public  Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Furniture> reader, ItemWriter<Furniture> writer,
                       ItemProcessor<Furniture, Furniture> processor) {
        return  stepBuilderFactory.get("step1").<Furniture, Furniture> chunk(10).reader(reader)
                .processor(processor).writer(writer).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

/*

    public Step step1(){
        return  stepBuilderFactory.get("step1").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Simple");
                return null;
            }
        }).build();
    }


    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job1").incrementer(new RunIdIncrementer()).start(step1).build();
    }
*/



}
