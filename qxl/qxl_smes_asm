; QXL_SMES.ASM Adds a message to the send list / buffer

        ASSUME ds:DGROUP
        ASSUME es:DGROUP
;	AX	pointer to message header (word length in words)
;	CX	length in words to fill in
;	DI	smashed
;	CC 	NC OK, C queue full	

qxl_smess:
	cli

	mov	di, ax
	mov	[di], cx
; push	si
; mov	si, OFFSET comm_count
; add	di, 2
; call	deb_cnt_buff
; pop	si
	mov	di, qxl_smqui		 ; messages queue in
	stosw					 ; put message in queue
	cmp	di, OFFSET qxl_smqutop	 ; end of buffer?
	jb	qsm_pup
	mov	di, OFFSET qxl_smqu	 ; ... reset to start
qsm_pup:
	cmp	di, qxl_smquo		 ; queue full?
	jz	qsm_full 			 ; ... yes

	mov	qxl_smqui, di
	clc
	sti

	retn

qsm_full:
	stc
	sti
	retn
	
	
