mock:
	rm mock_cdn_data.csv
	time python mock_data.py
	cat mock_cdn_data.csv | wc -l
	cat mock_cdn_data.csv | grep "Dirty" | wc -l
