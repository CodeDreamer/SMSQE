; QXL_PCST.ASM Sends the init message describing the PC configuration

        ASSUME ds:DGROUP
        ASSUME es:DGROUP
;	AX	smashed
;     CX    smashed
;	DI	smashed

qxl_pcset:
	lodsw				 ; skip spare bytes in message
	cli
	call	qxl_resetio		 ; reset the IO
	mov	ax, qxl_smquo
	mov	qxl_smqui,ax	 ; clear message queue
	sti

        mov     ax, OFFSET mes1_head
	mov	cx, 8/2
qps_retry:
	call	qxl_smess		 ; send this message
	jc	qps_retry
	jmp	qxl_rxm_loop
