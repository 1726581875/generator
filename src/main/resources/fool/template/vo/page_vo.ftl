package ${basePackageName};
import java.util.List;
import java.io.Serializable;
<#if isMyBatisPlus ! false>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
<#elseif isMyBatis ! false>
import com.github.pagehelper.PageInfo;
</#if>
<#if isJpa ! false>
import org.springframework.data.domain.Page;
</#if>
<#if isLombok ! false>
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
</#if>
/**
 * @author ${author.author}
 * @date ${author.date}
 * 封装分页结果
 */
<#if isLombok ! false>
@Data
@NoArgsConstructor
@AllArgsConstructor
</#if>
public class PageVO<T>  implements Serializable{
	/**
	 * 当前第几页
	 */
	private Integer pageIndex;
	/**
	 * 每页大小
	 */
	private Integer pageSize;
	/**
	 * 总页数
	 */
	private Integer pageCount;
	/**
	 * 数据总行数
	 */
	private Long totalRow;
	/**
	 * 页数据
	 */
	private List<T> content;

<#if isJpa ! false>
	public PageVO(Page<T> page) {
		this.pageIndex = page.getNumber();
		this.pageSize = page.getSize();
		this.pageCount = page.getTotalPages();
		this.totalRow = page.getTotalElements();
		this.content = page.getContent();
	}
</#if>

<#if isMyBatisPlus ! false>
	public PageVO() {}
	public PageVO(Page<T> page){
		this.pageIndex = Math.toIntExact(page.getCurrent());
		this.pageSize = Math.toIntExact(page.getSize());
		this.pageCount = Math.toIntExact(page.getPages());
		this.totalRow = page.getMaxLimit();
		this.content = page.getRecords();
	}
<#elseif isMyBatis ! false>
	public PageVO() {}

	public PageVO(PageInfo<T> pageInfo) {
		this.setPageIndex(pageInfo.getPages());
		this.setPageSize(pageInfo.getPageSize());
		this.setPageCount(pageInfo.getPages());
		this.setContent(pageInfo.getList());
		this.setTotalRow(pageInfo.getTotal());
	}
</#if>

<#if !isLombok ! false>
	public Integer getPageIndex() {
	    return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
	    this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
	  	return pageSize;
	}

	public void setPageSize(Integer pageSize) {
	    this.pageSize = pageSize;
	}

	public Integer getPageCount() {
		  return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		  this.pageCount = pageCount;
	}

	public Long getTotalRow() {
	  	return totalRow;
	}

	public void setTotalRow(Long totalRow) {
	    this.totalRow = totalRow;
	}

	public List<T> getContent() {
		  return content;
	}

	public void setContent(List<T> content) {
		  this.content = content;
	}
	
	@Override
	public String toString() {
		return "PageVO{" +
				"pageIndex=" + pageIndex +
				", pageSize=" + pageSize +
				", pageCount=" + pageCount +
				", totalRow=" + totalRow +
				", content=" + content +
				'}';
	}
</#if>
}
