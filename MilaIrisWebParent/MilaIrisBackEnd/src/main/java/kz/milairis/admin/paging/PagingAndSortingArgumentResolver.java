package kz.milairis.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
			NativeWebRequest request, WebDataBinderFactory binderFactory) {
		PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
		String sortDir = request.getParameter("sortDir");
		String sortField = request.getParameter("sortField");
		String keyword = request.getParameter("keyword");

		String reverseSortDir = Objects.equals(sortDir, "asc") ? "desc" : "asc";
		Objects.requireNonNull(model).addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleURL", Objects.requireNonNull(annotation).moduleURL());
		
		
		return new PagingAndSortingHelper(model, annotation.listName(),
				sortField, sortDir, keyword);
	}
}
