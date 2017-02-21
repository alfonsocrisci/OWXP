/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.micro.maintainance.analysis.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.micro.maintainance.analysis.exception.NoSuchEntryException;
import com.liferay.micro.maintainance.analysis.model.AnalysisEntry;
import com.liferay.micro.maintainance.analysis.model.impl.AnalysisEntryImpl;
import com.liferay.micro.maintainance.analysis.model.impl.AnalysisEntryModelImpl;
import com.liferay.micro.maintainance.analysis.service.persistence.AnalysisEntryPersistence;

import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the analysis entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalysisEntryPersistence
 * @see com.liferay.micro.maintainance.analysis.service.persistence.AnalysisEntryUtil
 * @generated
 */
@ProviderType
public class AnalysisEntryPersistenceImpl extends BasePersistenceImpl<AnalysisEntry>
	implements AnalysisEntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link AnalysisEntryUtil} to access the analysis entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = AnalysisEntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByUuid",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByUuid", new String[] { String.class.getName() },
			AnalysisEntryModelImpl.UUID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] { String.class.getName() });

	/**
	 * Returns all the analysis entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid(String uuid, int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid(String uuid, int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid, start, end, orderByComparator };
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if (!Objects.equals(uuid, analysisEntry.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByUuid_First(String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByUuid_First(uuid, orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByUuid_First(String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByUuid_Last(String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByUuid_Last(uuid, orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByUuid_Last(String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByUuid(uuid, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where uuid = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByUuid_PrevAndNext(long analysisId, String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(session, analysisEntry, uuid,
					orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByUuid_PrevAndNext(session, analysisEntry, uuid,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByUuid_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, String uuid,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid == null) {
			query.append(_FINDER_COLUMN_UUID_UUID_1);
		}
		else if (uuid.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (AnalysisEntry analysisEntry : findByUuid(uuid, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByUuid(String uuid) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID;

		Object[] finderArgs = new Object[] { uuid };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_1 = "analysisEntry.uuid IS NULL";
	private static final String _FINDER_COLUMN_UUID_UUID_2 = "analysisEntry.uuid = ?";
	private static final String _FINDER_COLUMN_UUID_UUID_3 = "(analysisEntry.uuid IS NULL OR analysisEntry.uuid = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByUuid_C",
			new String[] { String.class.getName(), Long.class.getName() },
			AnalysisEntryModelImpl.UUID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID_C = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] { String.class.getName(), Long.class.getName() });

	/**
	 * Returns all the analysis entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(uuid, companyId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid_C(String uuid, long companyId,
		int start, int end) {
		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid_C(String uuid, long companyId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByUuid_C(uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByUuid_C(String uuid, long companyId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C;
			finderArgs = new Object[] { uuid, companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C;
			finderArgs = new Object[] {
					uuid, companyId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if (!Objects.equals(uuid, analysisEntry.getUuid()) ||
							(companyId != analysisEntry.getCompanyId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByUuid_C_First(String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByUuid_C_First(uuid, companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByUuid_C_First(String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByUuid_C(uuid, companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByUuid_C_Last(String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByUuid_C_Last(uuid, companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByUuid_C_Last(String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByUuid_C(uuid, companyId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByUuid_C_PrevAndNext(long analysisId,
		String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByUuid_C_PrevAndNext(session, analysisEntry, uuid,
					companyId, orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByUuid_C_PrevAndNext(session, analysisEntry, uuid,
					companyId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByUuid_C_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, String uuid, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(5 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid == null) {
			query.append(_FINDER_COLUMN_UUID_C_UUID_1);
		}
		else if (uuid.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (AnalysisEntry analysisEntry : findByUuid_C(uuid, companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID_C;

		Object[] finderArgs = new Object[] { uuid, companyId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_1 = "analysisEntry.uuid IS NULL AND ";
	private static final String _FINDER_COLUMN_UUID_C_UUID_2 = "analysisEntry.uuid = ? AND ";
	private static final String _FINDER_COLUMN_UUID_C_UUID_3 = "(analysisEntry.uuid IS NULL OR analysisEntry.uuid = '') AND ";
	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 = "analysisEntry.companyId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ANALYSISID =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByAnalysisId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ANALYSISID =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByAnalysisId", new String[] { Long.class.getName() },
			AnalysisEntryModelImpl.ANALYSISID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ANALYSISID = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByAnalysisId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the analysis entries where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByAnalysisId(long analysisId) {
		return findByAnalysisId(analysisId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where analysisId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByAnalysisId(long analysisId, int start,
		int end) {
		return findByAnalysisId(analysisId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByAnalysisId(long analysisId, int start,
		int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByAnalysisId(analysisId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByAnalysisId(long analysisId, int start,
		int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ANALYSISID;
			finderArgs = new Object[] { analysisId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ANALYSISID;
			finderArgs = new Object[] { analysisId, start, end, orderByComparator };
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((analysisId != analysisEntry.getAnalysisId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_ANALYSISID_ANALYSISID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByAnalysisId_First(long analysisId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByAnalysisId_First(analysisId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByAnalysisId_First(long analysisId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByAnalysisId(analysisId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByAnalysisId_Last(long analysisId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByAnalysisId_Last(analysisId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByAnalysisId_Last(long analysisId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByAnalysisId(analysisId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByAnalysisId(analysisId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Removes all the analysis entries where analysisId = &#63; from the database.
	 *
	 * @param analysisId the analysis ID
	 */
	@Override
	public void removeByAnalysisId(long analysisId) {
		for (AnalysisEntry analysisEntry : findByAnalysisId(analysisId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where analysisId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByAnalysisId(long analysisId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_ANALYSISID;

		Object[] finderArgs = new Object[] { analysisId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_ANALYSISID_ANALYSISID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ANALYSISID_ANALYSISID_2 = "analysisEntry.analysisId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByCompanyId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCompanyId", new String[] { Long.class.getName() },
			AnalysisEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the analysis entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCompanyId(long companyId) {
		return findByCompanyId(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the analysis entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCompanyId(long companyId, int start,
		int end) {
		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCompanyId(long companyId, int start,
		int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCompanyId(long companyId, int start,
		int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId, start, end, orderByComparator };
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((companyId != analysisEntry.getCompanyId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByCompanyId_First(long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByCompanyId_First(companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByCompanyId_First(long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByCompanyId(companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByCompanyId_Last(long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByCompanyId_Last(companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByCompanyId_Last(long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByCompanyId(companyId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where companyId = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByCompanyId_PrevAndNext(long analysisId,
		long companyId, OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByCompanyId_PrevAndNext(session, analysisEntry,
					companyId, orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByCompanyId_PrevAndNext(session, analysisEntry,
					companyId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByCompanyId_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (AnalysisEntry analysisEntry : findByCompanyId(companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_COMPANYID;

		Object[] finderArgs = new Object[] { companyId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 = "analysisEntry.companyId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_A_C = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByA_C",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_C = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByA_C",
			new String[] { Long.class.getName(), Long.class.getName() },
			AnalysisEntryModelImpl.ANALYSISID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_A_C = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByA_C",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the analysis entries where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_C(long analysisId, long companyId) {
		return findByA_C(analysisId, companyId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where analysisId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_C(long analysisId, long companyId,
		int start, int end) {
		return findByA_C(analysisId, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_C(long analysisId, long companyId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByA_C(analysisId, companyId, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_C(long analysisId, long companyId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_C;
			finderArgs = new Object[] { analysisId, companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_A_C;
			finderArgs = new Object[] {
					analysisId, companyId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((analysisId != analysisEntry.getAnalysisId()) ||
							(companyId != analysisEntry.getCompanyId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_C_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_C_First(long analysisId, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_C_First(analysisId, companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_C_First(long analysisId, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByA_C(analysisId, companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_C_Last(long analysisId, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_C_Last(analysisId, companyId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_C_Last(long analysisId, long companyId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByA_C(analysisId, companyId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByA_C(analysisId, companyId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Removes all the analysis entries where analysisId = &#63; and companyId = &#63; from the database.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 */
	@Override
	public void removeByA_C(long analysisId, long companyId) {
		for (AnalysisEntry analysisEntry : findByA_C(analysisId, companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where analysisId = &#63; and companyId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param companyId the company ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByA_C(long analysisId, long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_A_C;

		Object[] finderArgs = new Object[] { analysisId, companyId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_C_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_C_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_A_C_ANALYSISID_2 = "analysisEntry.analysisId = ? AND ";
	private static final String _FINDER_COLUMN_A_C_COMPANYID_2 = "analysisEntry.companyId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_A_CM = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByA_CM",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_CM = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByA_CM",
			new String[] { Long.class.getName(), Long.class.getName() },
			AnalysisEntryModelImpl.ANALYSISID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CANMAINID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_A_CM = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByA_CM",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the analysis entries where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_CM(long analysisId, long canMainId) {
		return findByA_CM(analysisId, canMainId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where analysisId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_CM(long analysisId, long canMainId,
		int start, int end) {
		return findByA_CM(analysisId, canMainId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_CM(long analysisId, long canMainId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByA_CM(analysisId, canMainId, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_CM(long analysisId, long canMainId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_CM;
			finderArgs = new Object[] { analysisId, canMainId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_A_CM;
			finderArgs = new Object[] {
					analysisId, canMainId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((analysisId != analysisEntry.getAnalysisId()) ||
							(canMainId != analysisEntry.getCanMainId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_CM_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_CM_CANMAINID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(canMainId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_CM_First(long analysisId, long canMainId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_CM_First(analysisId, canMainId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", canMainId=");
		msg.append(canMainId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_CM_First(long analysisId, long canMainId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByA_CM(analysisId, canMainId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_CM_Last(long analysisId, long canMainId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_CM_Last(analysisId, canMainId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", canMainId=");
		msg.append(canMainId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_CM_Last(long analysisId, long canMainId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByA_CM(analysisId, canMainId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByA_CM(analysisId, canMainId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Removes all the analysis entries where analysisId = &#63; and canMainId = &#63; from the database.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 */
	@Override
	public void removeByA_CM(long analysisId, long canMainId) {
		for (AnalysisEntry analysisEntry : findByA_CM(analysisId, canMainId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where analysisId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param canMainId the can main ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByA_CM(long analysisId, long canMainId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_A_CM;

		Object[] finderArgs = new Object[] { analysisId, canMainId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_CM_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_CM_CANMAINID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(canMainId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_A_CM_ANALYSISID_2 = "analysisEntry.analysisId = ? AND ";
	private static final String _FINDER_COLUMN_A_CM_CANMAINID_2 = "analysisEntry.canMainId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_A_U_CM = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByA_U_CM",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U_CM =
		new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByA_U_CM",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			AnalysisEntryModelImpl.ANALYSISID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.USERID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CANMAINID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_A_U_CM = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByA_U_CM",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			});

	/**
	 * Returns all the analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_U_CM(long analysisId, long userId,
		long canMainId) {
		return findByA_U_CM(analysisId, userId, canMainId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_U_CM(long analysisId, long userId,
		long canMainId, int start, int end) {
		return findByA_U_CM(analysisId, userId, canMainId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_U_CM(long analysisId, long userId,
		long canMainId, int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByA_U_CM(analysisId, userId, canMainId, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByA_U_CM(long analysisId, long userId,
		long canMainId, int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U_CM;
			finderArgs = new Object[] { analysisId, userId, canMainId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_A_U_CM;
			finderArgs = new Object[] {
					analysisId, userId, canMainId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((analysisId != analysisEntry.getAnalysisId()) ||
							(userId != analysisEntry.getUserId()) ||
							(canMainId != analysisEntry.getCanMainId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_U_CM_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_U_CM_USERID_2);

			query.append(_FINDER_COLUMN_A_U_CM_CANMAINID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(userId);

				qPos.add(canMainId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_U_CM_First(long analysisId, long userId,
		long canMainId, OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_U_CM_First(analysisId, userId,
				canMainId, orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(", canMainId=");
		msg.append(canMainId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_U_CM_First(long analysisId, long userId,
		long canMainId, OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByA_U_CM(analysisId, userId, canMainId,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByA_U_CM_Last(long analysisId, long userId,
		long canMainId, OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByA_U_CM_Last(analysisId, userId,
				canMainId, orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("analysisId=");
		msg.append(analysisId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(", canMainId=");
		msg.append(canMainId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByA_U_CM_Last(long analysisId, long userId,
		long canMainId, OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByA_U_CM(analysisId, userId, canMainId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByA_U_CM(analysisId, userId, canMainId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Removes all the analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63; from the database.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 */
	@Override
	public void removeByA_U_CM(long analysisId, long userId, long canMainId) {
		for (AnalysisEntry analysisEntry : findByA_U_CM(analysisId, userId,
				canMainId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where analysisId = &#63; and userId = &#63; and canMainId = &#63;.
	 *
	 * @param analysisId the analysis ID
	 * @param userId the user ID
	 * @param canMainId the can main ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByA_U_CM(long analysisId, long userId, long canMainId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_A_U_CM;

		Object[] finderArgs = new Object[] { analysisId, userId, canMainId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_A_U_CM_ANALYSISID_2);

			query.append(_FINDER_COLUMN_A_U_CM_USERID_2);

			query.append(_FINDER_COLUMN_A_U_CM_CANMAINID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(analysisId);

				qPos.add(userId);

				qPos.add(canMainId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_A_U_CM_ANALYSISID_2 = "analysisEntry.analysisId = ? AND ";
	private static final String _FINDER_COLUMN_A_U_CM_USERID_2 = "analysisEntry.userId = ? AND ";
	private static final String _FINDER_COLUMN_A_U_CM_CANMAINID_2 = "analysisEntry.canMainId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_CM_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByCM_U",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CM_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCM_U",
			new String[] { Long.class.getName(), Long.class.getName() },
			AnalysisEntryModelImpl.CANMAINID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.USERID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_CM_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCM_U",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the analysis entries where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCM_U(long canMainId, long userId) {
		return findByCM_U(canMainId, userId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where canMainId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCM_U(long canMainId, long userId,
		int start, int end) {
		return findByCM_U(canMainId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where canMainId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCM_U(long canMainId, long userId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByCM_U(canMainId, userId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where canMainId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByCM_U(long canMainId, long userId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CM_U;
			finderArgs = new Object[] { canMainId, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_CM_U;
			finderArgs = new Object[] {
					canMainId, userId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((canMainId != analysisEntry.getCanMainId()) ||
							(userId != analysisEntry.getUserId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_CM_U_CANMAINID_2);

			query.append(_FINDER_COLUMN_CM_U_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(canMainId);

				qPos.add(userId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByCM_U_First(long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByCM_U_First(canMainId, userId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("canMainId=");
		msg.append(canMainId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByCM_U_First(long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByCM_U(canMainId, userId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByCM_U_Last(long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByCM_U_Last(canMainId, userId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("canMainId=");
		msg.append(canMainId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByCM_U_Last(long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByCM_U(canMainId, userId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByCM_U(canMainId, userId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where canMainId = &#63; and userId = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByCM_U_PrevAndNext(long analysisId,
		long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByCM_U_PrevAndNext(session, analysisEntry, canMainId,
					userId, orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByCM_U_PrevAndNext(session, analysisEntry, canMainId,
					userId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByCM_U_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, long canMainId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(5 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		query.append(_FINDER_COLUMN_CM_U_CANMAINID_2);

		query.append(_FINDER_COLUMN_CM_U_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(canMainId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where canMainId = &#63; and userId = &#63; from the database.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 */
	@Override
	public void removeByCM_U(long canMainId, long userId) {
		for (AnalysisEntry analysisEntry : findByCM_U(canMainId, userId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where canMainId = &#63; and userId = &#63;.
	 *
	 * @param canMainId the can main ID
	 * @param userId the user ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByCM_U(long canMainId, long userId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_CM_U;

		Object[] finderArgs = new Object[] { canMainId, userId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_CM_U_CANMAINID_2);

			query.append(_FINDER_COLUMN_CM_U_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(canMainId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_CM_U_CANMAINID_2 = "analysisEntry.canMainId = ? AND ";
	private static final String _FINDER_COLUMN_CM_U_USERID_2 = "analysisEntry.userId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_C_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByC_U",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByC_U",
			new String[] { Long.class.getName(), Long.class.getName() },
			AnalysisEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.USERID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_C_U = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_U",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the analysis entries where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByC_U(long companyId, long userId) {
		return findByC_U(companyId, userId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByC_U(long companyId, long userId,
		int start, int end) {
		return findByC_U(companyId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByC_U(long companyId, long userId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByC_U(companyId, userId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByC_U(long companyId, long userId,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_U;
			finderArgs = new Object[] { companyId, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_C_U;
			finderArgs = new Object[] {
					companyId, userId,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((companyId != analysisEntry.getCompanyId()) ||
							(userId != analysisEntry.getUserId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_U_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_U_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(userId);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByC_U_First(long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByC_U_First(companyId, userId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByC_U_First(long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByC_U(companyId, userId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByC_U_Last(long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByC_U_Last(companyId, userId,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByC_U_Last(long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByC_U(companyId, userId);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByC_U(companyId, userId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByC_U_PrevAndNext(long analysisId,
		long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByC_U_PrevAndNext(session, analysisEntry, companyId,
					userId, orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByC_U_PrevAndNext(session, analysisEntry, companyId,
					userId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByC_U_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, long companyId, long userId,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(5 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		query.append(_FINDER_COLUMN_C_U_COMPANYID_2);

		query.append(_FINDER_COLUMN_C_U_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where companyId = &#63; and userId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 */
	@Override
	public void removeByC_U(long companyId, long userId) {
		for (AnalysisEntry analysisEntry : findByC_U(companyId, userId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByC_U(long companyId, long userId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_C_U;

		Object[] finderArgs = new Object[] { companyId, userId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_U_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_U_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_U_COMPANYID_2 = "analysisEntry.companyId = ? AND ";
	private static final String _FINDER_COLUMN_C_U_USERID_2 = "analysisEntry.userId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_U_CD = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByU_cD",
			new String[] {
				Long.class.getName(), Date.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_U_CD = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED,
			AnalysisEntryImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByU_cD",
			new String[] { Long.class.getName(), Date.class.getName() },
			AnalysisEntryModelImpl.USERID_COLUMN_BITMASK |
			AnalysisEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_U_CD = new FinderPath(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByU_cD",
			new String[] { Long.class.getName(), Date.class.getName() });

	/**
	 * Returns all the analysis entries where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @return the matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByU_cD(long userId, Date createDate) {
		return findByU_cD(userId, createDate, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries where userId = &#63; and createDate = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByU_cD(long userId, Date createDate,
		int start, int end) {
		return findByU_cD(userId, createDate, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries where userId = &#63; and createDate = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByU_cD(long userId, Date createDate,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator) {
		return findByU_cD(userId, createDate, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the analysis entries where userId = &#63; and createDate = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching analysis entries
	 */
	@Override
	public List<AnalysisEntry> findByU_cD(long userId, Date createDate,
		int start, int end, OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_U_CD;
			finderArgs = new Object[] { userId, createDate };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_U_CD;
			finderArgs = new Object[] {
					userId, createDate,
					
					start, end, orderByComparator
				};
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AnalysisEntry analysisEntry : list) {
					if ((userId != analysisEntry.getUserId()) ||
							!Objects.equals(createDate,
								analysisEntry.getCreateDate())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_U_CD_USERID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_U_CD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_U_CD_CREATEDATE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analysis entry in the ordered set where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByU_cD_First(long userId, Date createDate,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByU_cD_First(userId, createDate,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first analysis entry in the ordered set where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByU_cD_First(long userId, Date createDate,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		List<AnalysisEntry> list = findByU_cD(userId, createDate, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analysis entry in the ordered set where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry
	 * @throws NoSuchEntryException if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry findByU_cD_Last(long userId, Date createDate,
		OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByU_cD_Last(userId, createDate,
				orderByComparator);

		if (analysisEntry != null) {
			return analysisEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last analysis entry in the ordered set where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analysis entry, or <code>null</code> if a matching analysis entry could not be found
	 */
	@Override
	public AnalysisEntry fetchByU_cD_Last(long userId, Date createDate,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		int count = countByU_cD(userId, createDate);

		if (count == 0) {
			return null;
		}

		List<AnalysisEntry> list = findByU_cD(userId, createDate, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analysis entries before and after the current analysis entry in the ordered set where userId = &#63; and createDate = &#63;.
	 *
	 * @param analysisId the primary key of the current analysis entry
	 * @param userId the user ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry[] findByU_cD_PrevAndNext(long analysisId, long userId,
		Date createDate, OrderByComparator<AnalysisEntry> orderByComparator)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = findByPrimaryKey(analysisId);

		Session session = null;

		try {
			session = openSession();

			AnalysisEntry[] array = new AnalysisEntryImpl[3];

			array[0] = getByU_cD_PrevAndNext(session, analysisEntry, userId,
					createDate, orderByComparator, true);

			array[1] = analysisEntry;

			array[2] = getByU_cD_PrevAndNext(session, analysisEntry, userId,
					createDate, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalysisEntry getByU_cD_PrevAndNext(Session session,
		AnalysisEntry analysisEntry, long userId, Date createDate,
		OrderByComparator<AnalysisEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(5 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE);

		query.append(_FINDER_COLUMN_U_CD_USERID_2);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_U_CD_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_U_CD_CREATEDATE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AnalysisEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analysisEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalysisEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analysis entries where userId = &#63; and createDate = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 */
	@Override
	public void removeByU_cD(long userId, Date createDate) {
		for (AnalysisEntry analysisEntry : findByU_cD(userId, createDate,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries where userId = &#63; and createDate = &#63;.
	 *
	 * @param userId the user ID
	 * @param createDate the create date
	 * @return the number of matching analysis entries
	 */
	@Override
	public int countByU_cD(long userId, Date createDate) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_U_CD;

		Object[] finderArgs = new Object[] { userId, createDate };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYSISENTRY_WHERE);

			query.append(_FINDER_COLUMN_U_CD_USERID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_U_CD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_U_CD_CREATEDATE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_U_CD_USERID_2 = "analysisEntry.userId = ? AND ";
	private static final String _FINDER_COLUMN_U_CD_CREATEDATE_1 = "analysisEntry.createDate IS NULL";
	private static final String _FINDER_COLUMN_U_CD_CREATEDATE_2 = "analysisEntry.createDate = ?";

	public AnalysisEntryPersistenceImpl() {
		setModelClass(AnalysisEntry.class);
	}

	/**
	 * Caches the analysis entry in the entity cache if it is enabled.
	 *
	 * @param analysisEntry the analysis entry
	 */
	@Override
	public void cacheResult(AnalysisEntry analysisEntry) {
		entityCache.putResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryImpl.class, analysisEntry.getPrimaryKey(),
			analysisEntry);

		analysisEntry.resetOriginalValues();
	}

	/**
	 * Caches the analysis entries in the entity cache if it is enabled.
	 *
	 * @param analysisEntries the analysis entries
	 */
	@Override
	public void cacheResult(List<AnalysisEntry> analysisEntries) {
		for (AnalysisEntry analysisEntry : analysisEntries) {
			if (entityCache.getResult(
						AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
						AnalysisEntryImpl.class, analysisEntry.getPrimaryKey()) == null) {
				cacheResult(analysisEntry);
			}
			else {
				analysisEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all analysis entries.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(AnalysisEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the analysis entry.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AnalysisEntry analysisEntry) {
		entityCache.removeResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryImpl.class, analysisEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<AnalysisEntry> analysisEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (AnalysisEntry analysisEntry : analysisEntries) {
			entityCache.removeResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
				AnalysisEntryImpl.class, analysisEntry.getPrimaryKey());
		}
	}

	/**
	 * Creates a new analysis entry with the primary key. Does not add the analysis entry to the database.
	 *
	 * @param analysisId the primary key for the new analysis entry
	 * @return the new analysis entry
	 */
	@Override
	public AnalysisEntry create(long analysisId) {
		AnalysisEntry analysisEntry = new AnalysisEntryImpl();

		analysisEntry.setNew(true);
		analysisEntry.setPrimaryKey(analysisId);

		String uuid = PortalUUIDUtil.generate();

		analysisEntry.setUuid(uuid);

		analysisEntry.setCompanyId(companyProvider.getCompanyId());

		return analysisEntry;
	}

	/**
	 * Removes the analysis entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param analysisId the primary key of the analysis entry
	 * @return the analysis entry that was removed
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry remove(long analysisId) throws NoSuchEntryException {
		return remove((Serializable)analysisId);
	}

	/**
	 * Removes the analysis entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the analysis entry
	 * @return the analysis entry that was removed
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry remove(Serializable primaryKey)
		throws NoSuchEntryException {
		Session session = null;

		try {
			session = openSession();

			AnalysisEntry analysisEntry = (AnalysisEntry)session.get(AnalysisEntryImpl.class,
					primaryKey);

			if (analysisEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(analysisEntry);
		}
		catch (NoSuchEntryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AnalysisEntry removeImpl(AnalysisEntry analysisEntry) {
		analysisEntry = toUnwrappedModel(analysisEntry);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(analysisEntry)) {
				analysisEntry = (AnalysisEntry)session.get(AnalysisEntryImpl.class,
						analysisEntry.getPrimaryKeyObj());
			}

			if (analysisEntry != null) {
				session.delete(analysisEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (analysisEntry != null) {
			clearCache(analysisEntry);
		}

		return analysisEntry;
	}

	@Override
	public AnalysisEntry updateImpl(AnalysisEntry analysisEntry) {
		analysisEntry = toUnwrappedModel(analysisEntry);

		boolean isNew = analysisEntry.isNew();

		AnalysisEntryModelImpl analysisEntryModelImpl = (AnalysisEntryModelImpl)analysisEntry;

		if (Validator.isNull(analysisEntry.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			analysisEntry.setUuid(uuid);
		}

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (analysisEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				analysisEntry.setCreateDate(now);
			}
			else {
				analysisEntry.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!analysisEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				analysisEntry.setModifiedDate(now);
			}
			else {
				analysisEntry.setModifiedDate(serviceContext.getModifiedDate(
						now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (analysisEntry.isNew()) {
				session.save(analysisEntry);

				analysisEntry.setNew(false);
			}
			else {
				analysisEntry = (AnalysisEntry)session.merge(analysisEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !AnalysisEntryModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalUuid()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);

				args = new Object[] { analysisEntryModelImpl.getUuid() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalUuid(),
						analysisEntryModelImpl.getOriginalCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getUuid(),
						analysisEntryModelImpl.getCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ANALYSISID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalAnalysisId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_ANALYSISID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ANALYSISID,
					args);

				args = new Object[] { analysisEntryModelImpl.getAnalysisId() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_ANALYSISID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ANALYSISID,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_COMPANYID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);

				args = new Object[] { analysisEntryModelImpl.getCompanyId() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_COMPANYID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalAnalysisId(),
						analysisEntryModelImpl.getOriginalCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_C, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_C,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getAnalysisId(),
						analysisEntryModelImpl.getCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_C, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_C,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_CM.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalAnalysisId(),
						analysisEntryModelImpl.getOriginalCanMainId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_CM, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_CM,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getAnalysisId(),
						analysisEntryModelImpl.getCanMainId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_CM, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_CM,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U_CM.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalAnalysisId(),
						analysisEntryModelImpl.getOriginalUserId(),
						analysisEntryModelImpl.getOriginalCanMainId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_U_CM, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U_CM,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getAnalysisId(),
						analysisEntryModelImpl.getUserId(),
						analysisEntryModelImpl.getCanMainId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_U_CM, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U_CM,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CM_U.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalCanMainId(),
						analysisEntryModelImpl.getOriginalUserId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_CM_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CM_U,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getCanMainId(),
						analysisEntryModelImpl.getUserId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_CM_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CM_U,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_U.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalCompanyId(),
						analysisEntryModelImpl.getOriginalUserId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_C_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_U,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getCompanyId(),
						analysisEntryModelImpl.getUserId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_C_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_U,
					args);
			}

			if ((analysisEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_U_CD.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analysisEntryModelImpl.getOriginalUserId(),
						analysisEntryModelImpl.getOriginalCreateDate()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_U_CD, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_U_CD,
					args);

				args = new Object[] {
						analysisEntryModelImpl.getUserId(),
						analysisEntryModelImpl.getCreateDate()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_U_CD, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_U_CD,
					args);
			}
		}

		entityCache.putResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
			AnalysisEntryImpl.class, analysisEntry.getPrimaryKey(),
			analysisEntry, false);

		analysisEntry.resetOriginalValues();

		return analysisEntry;
	}

	protected AnalysisEntry toUnwrappedModel(AnalysisEntry analysisEntry) {
		if (analysisEntry instanceof AnalysisEntryImpl) {
			return analysisEntry;
		}

		AnalysisEntryImpl analysisEntryImpl = new AnalysisEntryImpl();

		analysisEntryImpl.setNew(analysisEntry.isNew());
		analysisEntryImpl.setPrimaryKey(analysisEntry.getPrimaryKey());

		analysisEntryImpl.setUuid(analysisEntry.getUuid());
		analysisEntryImpl.setAnalysisId(analysisEntry.getAnalysisId());
		analysisEntryImpl.setCompanyId(analysisEntry.getCompanyId());
		analysisEntryImpl.setUserId(analysisEntry.getUserId());
		analysisEntryImpl.setUserName(analysisEntry.getUserName());
		analysisEntryImpl.setCreateDate(analysisEntry.getCreateDate());
		analysisEntryImpl.setModifiedDate(analysisEntry.getModifiedDate());
		analysisEntryImpl.setCanMainId(analysisEntry.getCanMainId());
		analysisEntryImpl.setAnalysisData(analysisEntry.getAnalysisData());

		return analysisEntryImpl;
	}

	/**
	 * Returns the analysis entry with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the analysis entry
	 * @return the analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryException {
		AnalysisEntry analysisEntry = fetchByPrimaryKey(primaryKey);

		if (analysisEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return analysisEntry;
	}

	/**
	 * Returns the analysis entry with the primary key or throws a {@link NoSuchEntryException} if it could not be found.
	 *
	 * @param analysisId the primary key of the analysis entry
	 * @return the analysis entry
	 * @throws NoSuchEntryException if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry findByPrimaryKey(long analysisId)
		throws NoSuchEntryException {
		return findByPrimaryKey((Serializable)analysisId);
	}

	/**
	 * Returns the analysis entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the analysis entry
	 * @return the analysis entry, or <code>null</code> if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
				AnalysisEntryImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		AnalysisEntry analysisEntry = (AnalysisEntry)serializable;

		if (analysisEntry == null) {
			Session session = null;

			try {
				session = openSession();

				analysisEntry = (AnalysisEntry)session.get(AnalysisEntryImpl.class,
						primaryKey);

				if (analysisEntry != null) {
					cacheResult(analysisEntry);
				}
				else {
					entityCache.putResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
						AnalysisEntryImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
					AnalysisEntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return analysisEntry;
	}

	/**
	 * Returns the analysis entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param analysisId the primary key of the analysis entry
	 * @return the analysis entry, or <code>null</code> if a analysis entry with the primary key could not be found
	 */
	@Override
	public AnalysisEntry fetchByPrimaryKey(long analysisId) {
		return fetchByPrimaryKey((Serializable)analysisId);
	}

	@Override
	public Map<Serializable, AnalysisEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, AnalysisEntry> map = new HashMap<Serializable, AnalysisEntry>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			AnalysisEntry analysisEntry = fetchByPrimaryKey(primaryKey);

			if (analysisEntry != null) {
				map.put(primaryKey, analysisEntry);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
					AnalysisEntryImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (AnalysisEntry)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_ANALYSISENTRY_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (AnalysisEntry analysisEntry : (List<AnalysisEntry>)q.list()) {
				map.put(analysisEntry.getPrimaryKeyObj(), analysisEntry);

				cacheResult(analysisEntry);

				uncachedPrimaryKeys.remove(analysisEntry.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(AnalysisEntryModelImpl.ENTITY_CACHE_ENABLED,
					AnalysisEntryImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the analysis entries.
	 *
	 * @return the analysis entries
	 */
	@Override
	public List<AnalysisEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analysis entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @return the range of analysis entries
	 */
	@Override
	public List<AnalysisEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the analysis entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of analysis entries
	 */
	@Override
	public List<AnalysisEntry> findAll(int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the analysis entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AnalysisEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of analysis entries
	 * @param end the upper bound of the range of analysis entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of analysis entries
	 */
	@Override
	public List<AnalysisEntry> findAll(int start, int end,
		OrderByComparator<AnalysisEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<AnalysisEntry> list = null;

		if (retrieveFromCache) {
			list = (List<AnalysisEntry>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_ANALYSISENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ANALYSISENTRY;

				if (pagination) {
					sql = sql.concat(AnalysisEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalysisEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the analysis entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AnalysisEntry analysisEntry : findAll()) {
			remove(analysisEntry);
		}
	}

	/**
	 * Returns the number of analysis entries.
	 *
	 * @return the number of analysis entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ANALYSISENTRY);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return AnalysisEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the analysis entry persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(AnalysisEntryImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;
	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;
	private static final String _SQL_SELECT_ANALYSISENTRY = "SELECT analysisEntry FROM AnalysisEntry analysisEntry";
	private static final String _SQL_SELECT_ANALYSISENTRY_WHERE_PKS_IN = "SELECT analysisEntry FROM AnalysisEntry analysisEntry WHERE analysisId IN (";
	private static final String _SQL_SELECT_ANALYSISENTRY_WHERE = "SELECT analysisEntry FROM AnalysisEntry analysisEntry WHERE ";
	private static final String _SQL_COUNT_ANALYSISENTRY = "SELECT COUNT(analysisEntry) FROM AnalysisEntry analysisEntry";
	private static final String _SQL_COUNT_ANALYSISENTRY_WHERE = "SELECT COUNT(analysisEntry) FROM AnalysisEntry analysisEntry WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "analysisEntry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No AnalysisEntry exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No AnalysisEntry exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(AnalysisEntryPersistenceImpl.class);
	private static final Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"uuid"
			});
}