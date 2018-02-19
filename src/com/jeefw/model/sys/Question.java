package com.jeefw.model.sys;

import static org.hibernate.search.annotations.FieldCacheType.CLASS;
import static org.hibernate.search.annotations.FieldCacheType.ID;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy; 
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.CacheFromIndex;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.google.common.base.Objects; 


@Entity
@Table(name = "question")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount", "sortColumns", "cmd", "queryDynamicConditions", "sortedConditions", "dynamicProperties", "success", "message", "sortColumnsString", "flag" })
@Indexed
@CacheFromIndex({ CLASS, ID })
public class Question {

	private static final long serialVersionUID = 7306241552815502398L;
	@DocumentId
	@Id
	@GeneratedValue
	@Column(name = "questionId")
	private Long questionId;
	@Field(index = Index.YES, analyze=Analyze.YES, store = Store.YES)
	@Column(name = "question", length = 512, nullable = false)
	private String question;

	public Question() {

	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Question other = (Question) obj;
		return Objects.equal(this.questionId, other.questionId) && Objects.equal(this.question, other.question);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.questionId, this.question);
	}

}
