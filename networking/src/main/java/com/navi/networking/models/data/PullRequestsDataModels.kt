package com.navi.networking.models.data

import com.squareup.moshi.Json

data class PullRequestDataModel(
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "node_id") val nodeId: String?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "user") val user: UserDataModel?,
    @field:Json(name = "body") val message: String?,
    @field:Json(name = "labels") val labels: List<LabelDataModel?>?,
    @field:Json(name = "milestone") val milestone: List<MilestoneDataModel?>?,
    @field:Json(name = "created_at") val createdDate: String?,
    @field:Json(name = "updated_at") val updatedDate: String?,
    @field:Json(name = "closed_at") val closedDate: String?,
    @field:Json(name = "merged_at") val mergedDate: String?,
    @field:Json(name = "merge_commit_sha") val mergeCommitId: String?,
    @field:Json(name = "assignee") val assignee: UserDataModel?,
    @field:Json(name = "assignees") val assignees: List<UserDataModel?>?,
    @field:Json(name = "requested_reviewers") val requestedReviewers: List<UserDataModel?>?,
    @field:Json(name = "head") val headBranch: BranchDataModel?,
    @field:Json(name = "base") val baseBranch: BranchDataModel?,
    @field:Json(name = "_links") val links: LinksDataModel?,
    @field:Json(name = "draft") val isDraft: Boolean?
)

data class UserDataModel(
    @field:Json(name = "login") val name: String?,
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "node_id") val nodeId: String?,
    @field:Json(name = "avatar_url") val image: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "site_admin") val isSiteAdmin: Boolean?
)

data class LabelDataModel(
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "color") val color: String?,
    @field:Json(name = "default") val isDefault: Boolean?
)

data class MilestoneDataModel(
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "node_id") val nodeId: String?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "creator") val creator: UserDataModel?,
    @field:Json(name = "open_issues") val openIssues: Long?,
    @field:Json(name = "closed_issues") val closedIssues: Long?,
    @field:Json(name = "created_at") val createdDate: String?,
    @field:Json(name = "updated_at") val updatedDate: String?,
    @field:Json(name = "closed_at") val closedDate: String?,
    @field:Json(name = "due_on") val dueOn: String?
)

data class BranchDataModel(
    @field:Json(name = "label") val label: String?,
    @field:Json(name = "ref") val ref: String?,
    @field:Json(name = "sha") val sha: String?,
    @field:Json(name = "user") val user: UserDataModel?,
    @field:Json(name = "repo") val repo: RepoDataModel?
)

data class RepoDataModel(
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "node_id") val nodeId: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "owner") val owner: UserDataModel?,
    @field:Json(name = "private") val isPrivate: Boolean?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "fork") val isForked: Boolean?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "forks_count") val forksCount: Long?,
    @field:Json(name = "watchers_count") val watchersCount: Long?,
    @field:Json(name = "default_branch") val defaultBranch: String?,
    @field:Json(name = "topics") val topics: List<String?>?,
    @field:Json(name = "visibility") val visibility: String?,
    @field:Json(name = "pushed_at") val pushedDate: String?,
    @field:Json(name = "created_at") val createdDate: String?,
    @field:Json(name = "updated_at") val updatedDate: String?,
    @field:Json(name = "permissions") val permissions: PermissionsDataModel?,
    @field:Json(name = "license") val license: LicenseDataModel?,
    @field:Json(name = "forks") val forks: Long?,
    @field:Json(name = "open_issues") val openIssues: Long?,
    @field:Json(name = "watchers") val watchers: Long?
)

data class LicenseDataModel(
    @field:Json(name = "key") val key: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "spdx_id") val spdxId: String?,
    @field:Json(name = "node_id") val nodeId: String?
)

data class PermissionsDataModel(
    @field:Json(name = "admin") val isAdmin: Boolean?,
    @field:Json(name = "push") val canPush: Boolean?,
    @field:Json(name = "pull") val canPull: Boolean?
)

data class LinksDataModel(
    @field:Json(name = "self") val self: HrefDataModel?,
    @field:Json(name = "html") val html: HrefDataModel?,
    @field:Json(name = "issue") val issue: HrefDataModel?,
    @field:Json(name = "comments") val comments: HrefDataModel?,
    @field:Json(name = "review_comments") val reviewComments: HrefDataModel?,
    @field:Json(name = "review_comment") val reviewComment: HrefDataModel?,
    @field:Json(name = "commits") val commits: HrefDataModel?,
    @field:Json(name = "statuses") val statuses: HrefDataModel?
)

data class HrefDataModel(
    @field:Json(name = "href") val href: String?
)